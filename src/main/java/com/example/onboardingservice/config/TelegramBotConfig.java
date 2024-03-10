package com.example.onboardingservice.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Configuration
public class TelegramBotConfig {

    @Bean
    @SneakyThrows
    public TelegramBotsApi registerBots(List<LongPollingBot> bots) {
        var botsApi = new TelegramBotsApi(DefaultBotSession.class);

        for (var bot : bots) {
            botsApi.registerBot(bot);
        }

        return botsApi;
    }
}
