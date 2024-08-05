package com.example.onboardingservice.bot;

import com.example.onboardingservice.config.properties.TelegramBotProperties;
import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.Action;
import com.example.onboardingservice.scenaries.handlers.ActionHandler;
import com.example.onboardingservice.scenaries.utils.ButtonActionUtils;
import com.example.onboardingservice.scenaries.utils.InitScenariosCommandUtils;
import com.example.onboardingservice.service.HrsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

@Slf4j
@Component
public class OnboardingBot extends TelegramLongPollingBot {
    private final TelegramBotProperties botProperties;
    private final ScenarioService scenarioService;
    private final HrsService hrsService;
    private final Map<Class<? extends Action>, ActionHandler<Action>> actionHandlers;

    public OnboardingBot(TelegramBotProperties properties,
                         ScenarioService service,
                         HrsService hrsService,
                         List<ActionHandler<? extends Action>> actionHandlers) {
        super(properties.getBotToken());
        this.botProperties = properties;
        this.scenarioService = service;
        this.hrsService = hrsService;
        this.actionHandlers = new HashMap<>();

        for (var actionHandler : actionHandlers) {
            //noinspection unchecked
            this.actionHandlers.put(actionHandler.getHandledClass(), (ActionHandler<Action>) actionHandler);
        }
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

        } else {
            final var messageText = update.getMessage().getText();

            chatId = update.getMessage().getChatId();
            context = scenarioService.buildContext(chatId);

            var eventType = InitScenariosCommandUtils.parseEventType(messageText);
            context.put(eventType.name(), messageText);
        }

        final var isTestUser = hrsService.isHrChatId(context.getChatId());
        context.put(ContextConstants.INCLUDE_TEST_SCENARIOS, isTestUser);

        //FIXME: переключать сценарии по запросу
        metadata = scenarioService.findActiveScenariosMetadata(context);
        // meta == null => new chat
        if (metadata == null) {
            metadata = scenarioService.initializeScenarios(context);
        } else {
            context = context.restore(scenarioService.findActiveContext(chatId));
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

                var handler = actionHandlers.get(action.getClass());
                var nextActionId = handler.process(action, this, update, context, metadata);

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
            scenarioService.saveScenariosMetadata(context, metadata);
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
