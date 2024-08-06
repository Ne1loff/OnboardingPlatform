package com.example.onboardingservice.scenaries.handlers;

import com.example.onboardingservice.bot.messages.KeyboardFactory;
import com.example.onboardingservice.bot.messages.MessageFactory;
import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.impl.SendMessageAction;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class SendMessageActionHandler implements ActionHandler<SendMessageAction> {

    public static final Pattern PROPERTY_PATTERN = Pattern.compile("(?<!\\\\)\\$([a-zA-Z_]*)", Pattern.MULTILINE);

    @Override
    public Class<SendMessageAction> getHandledClass() {
        return SendMessageAction.class;
    }

    @Override
    public Optional<UUID> process(SendMessageAction action,
                                  AbsSender sender,
                                  Update update,
                                  ActionContext context,
                                  ScenariosMetadata metadata) throws TelegramApiException {
        final Long chatId = context.getChatId();

        SendMessage message;
        var text = action.getText();
        var resultText = replaceProperties(text, context);

        if (action.hasButtons()) {
            var keyboard = KeyboardFactory.createKeyboard(action.getButtons(), chatId);
            message = action.isMarkdownText()
                    ? MessageFactory.createSendMessageMDViaKeyboard(chatId, resultText, keyboard)
                    : MessageFactory.createSendMessageViaKeyboard(chatId, resultText, keyboard);

        } else {
            message = action.isMarkdownText()
                    ? MessageFactory.createSendMessageMD(chatId, resultText)
                    : MessageFactory.createSendMessage(chatId, resultText);
        }

        sender.execute(message);

        return action.getNextActionId();
    }

    private String replaceProperties(String message, ActionContext context) {
        var matcher = PROPERTY_PATTERN.matcher(message);

        var sb = new StringBuilder();
        while (matcher.find()) {
            var propertyName = matcher.group(1);
            var property = context.get(propertyName);

            if (property == null) {
                throw new IllegalStateException("Defined property does not contains in context");
            }

            matcher.appendReplacement(sb, property);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
