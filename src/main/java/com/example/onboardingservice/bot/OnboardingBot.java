package com.example.onboardingservice.bot;

import com.example.onboardingservice.config.TelegramBotProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class OnboardingBot extends TelegramLongPollingBot {
    private final TelegramBotProperties botProperties;

    protected OnboardingBot(TelegramBotProperties properties) {
        super(properties.getBotToken());
        this.botProperties = properties;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return botProperties.getBotName();
    }
}
