package com.example.onboardingservice.bot;

import com.example.onboardingservice.config.TelegramBotProperties;
import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStartEventType;
import com.example.onboardingservice.scenaries.utils.ButtonActionUtils;
import com.example.onboardingservice.scenaries.utils.InitScenariosCommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Slf4j
@Component
public class OnboardingBot extends TelegramLongPollingBot {
    private final TelegramBotProperties botProperties;
    private final ScenarioService scenarioService;

    public OnboardingBot(TelegramBotProperties properties, ScenarioService service) {
        super(properties.getBotToken());
        this.botProperties = properties;
        this.scenarioService = service;
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
                .filter(route::hasAction)
                .map(route::next)
                .orElse(route.current());

        try {
            boolean hasNext;
            do {
                context.put(ContextConstants.ACTION_ID, action.getId());
                scenarioService.saveScenariosMetadata(context, metadata);

                action.onUpdate(this, update, context, metadata);

                hasNext = action.getNextActionId().isPresent() && route.hasAction(action.getNextActionId().get());
                if (hasNext) {
                    action = route.next(action.getNextActionId().get());
                }

                if (context.containsKey(ContextConstants.NEED_INIT) && context.get(ContextConstants.NEED_INIT)) {
                    context.put(ContextConstants.ACTION_ID, action.getId());

                    metadata = scenarioService.changeScenarios(context, metadata);
                    context = scenarioService.findActiveContext(context.getChatId());
                    route = metadata.getRoute();
                    action = route.current();
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
