package com.example.onboardingservice.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Configuration
public class TelegramBotConfig {

    @Bean
    @SneakyThrows
    public TelegramBotsApi registerBots(List<AbilityBot> bots) {
        var botsApi = new TelegramBotsApi(DefaultBotSession.class);

        for (AbilityBot bot : bots) {
            botsApi.registerBot(bot);
        }

        return botsApi;
    }
}
