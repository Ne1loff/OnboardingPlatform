package com.example.onboardingservice.messages;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.Function;

@UtilityClass
public class MessageFactory {

    public static final String MARKDOWN_V2 = "MarkdownV2";

    public SendMessage createSendMessage(Long chatId, String text) {
        return createSendMessageByBuilder(builder -> builder.text(text).chatId(chatId));
    }

    public SendMessage createSendMessageMD(Long chatId, String text) {
        return createSendMessageByBuilder(builder -> builder.text(text).chatId(chatId).parseMode(MARKDOWN_V2));
    }

    public SendMessage createSendMessageViaKeyboard(Long chatId, String text, ReplyKeyboard keyboard) {
        return createSendMessageByBuilder(builder -> builder.text(text).chatId(chatId).replyMarkup(keyboard));
    }

    public SendMessage createSendMessageMDViaKeyboard(Long chatId, String text, ReplyKeyboard keyboard) {
        return createSendMessageByBuilder(builder -> builder
                .text(text)
                .chatId(chatId)
                .parseMode(MARKDOWN_V2)
                .replyMarkup(keyboard));
    }

    private SendMessage createSendMessageByBuilder(
            Function<SendMessage.SendMessageBuilder, SendMessage.SendMessageBuilder> generator) {
        return generator.apply(SendMessage.builder()).build();
    }
}
