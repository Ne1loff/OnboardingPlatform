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

            context.putParameter(ContextConstants.CHAT_ID, chatId);
            context.putParameter(ContextConstants.SCENARIOS_NAME, callbackData.scenariosName());
            context.putParameter(ContextConstants.ACTION_ID, callbackData.actionId());
            context.putParameter(ScenariosStartEventType.BUTTON.name(), messageText);

        } else {
            final var messageText = update.getMessage().getText();

            chatId = update.getMessage().getChatId();
            context = scenarioService.buildContext(chatId);

            var eventType = InitScenariosCommandUtils.parseEventType(messageText);
            context.putParameter(eventType.name(), messageText);
        }

        metadata = scenarioService.findActiveScenariosMetadata(chatId);

        // meta == null => new chat
        if (metadata == null || context.getParameters().get(ContextConstants.SCENARIOS_NAME) != null) {
            metadata = scenarioService.initializeScenarios(context);
        }

        var route = metadata.getRoute();
        var action = route.current();

        try {
            boolean hasNext;
            do {
                context.putParameter(ContextConstants.ACTION_ID, action.getId());
                scenarioService.saveScenariosMetadata(context, metadata);

                action.onUpdate(this, update, context, metadata);

                hasNext = action.getNextActionId().isPresent() && route.hasAction(action.getNextActionId().get());
                if (hasNext) {
                    action = route.next(action.getNextActionId().get());
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
