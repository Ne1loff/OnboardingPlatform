package com.example.onboardingservice.bot;

import com.example.onboardingservice.config.TelegramBotProperties;
import com.example.onboardingservice.handler.ResponseHandler;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Component
public class OnboardingBot extends AbilityBot {

    private final ResponseHandler handler;

    protected OnboardingBot(TelegramBotProperties properties) {
        super(properties.getBotToken(), properties.getBotName());
        handler = new ResponseHandler(silent, db);
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    public Ability startBot() {
        return Ability.builder()
                .name("start")
                .info("Starts the bot")
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> handler.replyToStart(ctx.chatId()))
                .build();
    }

    public Reply replyToButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (bot, upd) -> handler.replyToButtons(getChatId(upd), upd.getMessage());
        return Reply.of(action, Flag.TEXT, update -> handler.userIsActive(getChatId(update)));
    }
}
