package com.example.onboardingservice.bot;

import com.example.onboardingservice.config.TelegramBotProperties;
import com.example.onboardingservice.scenaries.ScenarioService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    public void onUpdateReceived(Update update) {
        final var chatId = update.getMessage().getChatId();
        //var meta = scenarioService.findActiveScenariosMetadata(chatId);
    }

    @Override
    public String getBotUsername() {
        return botProperties.getBotName();
    }
}
