package com.example.onboardingservice.scenaries.actions;

import com.example.onboardingservice.messages.KeyboardFactory;
import com.example.onboardingservice.messages.MessageFactory;
import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public final class TextAction implements Action {

    private UUID id;
    private String name;
    private String text;
    private boolean isMarkdownText;
    private List<ActionButton> buttons;

    @Override
    public void onUpdate(AbsSender sender, Update update, ActionContext context, ScenariosMetadata metadata) throws TelegramApiException {
        Long chatId = context.getChatId();
        String scenarioId = metadata.getScenarioName();

        SendMessage message;
        if (hasButtons()) {
            var keyboard = KeyboardFactory.createKeyboard(buttons, chatId, scenarioId);
            message = isMarkdownText
                    ? MessageFactory.createSendMessageMDViaKeyboard(chatId, text, keyboard)
                    : MessageFactory.createSendMessageViaKeyboard(chatId, text, keyboard);

        } else {
            message = isMarkdownText
                    ? MessageFactory.createSendMessageMD(chatId, text)
                    : MessageFactory.createSendMessage(chatId, text);
        }

        sender.execute(message);
    }

    private boolean hasButtons() {
        return !buttons.isEmpty();
    }
}
