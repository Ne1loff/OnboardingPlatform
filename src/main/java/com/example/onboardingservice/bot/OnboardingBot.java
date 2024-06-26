package com.example.onboardingservice.bot;

import com.example.onboardingservice.config.properties.TelegramBotProperties;
import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.Action;
import com.example.onboardingservice.scenaries.handlers.ActionHandler;
import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStartEventType;
import com.example.onboardingservice.scenaries.utils.ButtonActionUtils;
import com.example.onboardingservice.scenaries.utils.InitScenariosCommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OnboardingBot extends TelegramLongPollingBot {
    private final TelegramBotProperties botProperties;
    private final ScenarioService scenarioService;
    private final List<ActionHandler<Action>> actionHandlers;

    public OnboardingBot(TelegramBotProperties properties, ScenarioService service, List<ActionHandler<? extends Action>> actionHandlers) {
        super(properties.getBotToken());
        this.botProperties = properties;
        this.scenarioService = service;
        this.actionHandlers = actionHandlers.stream().map(it -> (ActionHandler<Action>) it).toList();
    }

    @Override
    @Transactional
    public void onUpdateReceived(Update update) {
        final boolean isCallbackData = update.hasCallbackQuery();

        Long chatId;
        ActionContext context;
        ScenariosMetadata metadata;

        if (isCallbackData) {
            final var messageText = update.getCallbackQuery().getData();
            final var callbackData = ButtonActionUtils.parseButtonCallbackData(messageText);

            chatId = callbackData.chatId();
            context = scenarioService.buildContext(chatId);

            context.put(ContextConstants.CHAT_ID, chatId);
            context.put(ContextConstants.ACTION_ID, callbackData.actionId());
            context.put(ScenariosStartEventType.BUTTON.name(), messageText);

        } else {
            final var messageText = update.getMessage().getText();

            chatId = update.getMessage().getChatId();
            context = scenarioService.buildContext(chatId);

            var eventType = InitScenariosCommandUtils.parseEventType(messageText);
            context.put(eventType.name(), messageText);
        }

        metadata = scenarioService.findActiveScenariosMetadata(chatId);
        // meta == null => new chat
        if (metadata == null) {
            metadata = scenarioService.initializeScenarios(context);
        } else {
            context.restore(scenarioService.findActiveContext(chatId));
        }

        var route = metadata.getRoute();
        var action = Optional.ofNullable(context.get(ContextConstants.ACTION_ID))
                .map(UUID::fromString)
                .filter(route::hasAction)
                .map(route::next)
                .orElse(route.current());

        try {
            boolean hasNext;
            do {
                context.put(ContextConstants.ACTION_ID, action.getId());
                scenarioService.saveScenariosMetadata(context, metadata);

                final var finalAction = action;
                var handler = actionHandlers.stream().filter(it -> it.getHandledClass().equals(finalAction.getClass()))
                        .findFirst().orElseThrow();
                var nextActionId = handler.process(finalAction, this, update, context, metadata);

                hasNext = nextActionId.isPresent() && route.hasAction(nextActionId.get());
                if (hasNext) {
                    action = route.next(nextActionId.get());
                }

                if (context.containsKey(ContextConstants.NEED_INIT) && Boolean.parseBoolean(context.get(ContextConstants.NEED_INIT))) {
                    context.put(ContextConstants.ACTION_ID, action.getId());
                    context.put(ContextConstants.NEED_INIT, false);

                    metadata = scenarioService.changeScenarios(context, metadata);
                    context = scenarioService.findActiveContext(chatId);
                    if (context == null) {
                        context = scenarioService.buildContext(chatId);
                    }
                    route = metadata.getRoute();
                    action = route.current();
                    hasNext = true;
                }
            } while (hasNext);
        } catch (Exception exception) {
            log.error("Telegram API exception", exception);
            throw new RuntimeException(exception);
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getBotName();
    }
}
