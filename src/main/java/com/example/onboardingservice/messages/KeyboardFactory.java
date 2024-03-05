package com.example.onboardingservice.messages;

import com.example.onboardingservice.scenaries.actions.ActionButton;
import com.example.onboardingservice.scenaries.utils.ButtonActionUtils;
import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@UtilityClass
public class KeyboardFactory {

    public ReplyKeyboard mainMenuKeyboard() {
        var buttonTexts = List.of(
                "Задать вопрос",
                "Контакты HR",
                "Документы",
                "Как пройти");

        return createKeyboard(buttonTexts);
    }

    public ReplyKeyboard documentsKeyboard() {
        var buttonTexts = List.of(
                "Пропуск",
                "Отпуск",
                "Техника");

        return createKeyboard(buttonTexts);
    }

    // FIXME: Не создаются 3 кнопки
    private InlineKeyboardMarkup createKeyboard(List<String> buttonTexts) {
        var buttons = new LinkedList<List<InlineKeyboardButton>>();

        var inRow = 0;
        List<InlineKeyboardButton> buttonRow = null;
        for (var buttonText : buttonTexts) {
            if (buttonRow == null) {
                buttonRow = new ArrayList<>(2);
            }

            buttonRow.add(InlineKeyboardButton.builder().text(buttonText).callbackData(buttonText).build());

            if (++inRow % 2 == 0) {
                buttons.add(buttonRow);
                buttonRow = null;
            }
        }

        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup createKeyboard(List<ActionButton> buttons, Long chatId, String scenarioId) {
        var maxInRow = 3;
        var inlineButtons = new LinkedList<List<InlineKeyboardButton>>();

        var buttonsPartitions = Lists.partition(buttons, maxInRow);

        for (var partition : buttonsPartitions) {

            List<InlineKeyboardButton> buttonRow = new ArrayList<>(maxInRow);
            for (var button : partition) {
                var keyboardButton = InlineKeyboardButton.builder()
                        .text(button.getText())
                        .callbackData(ButtonActionUtils.generateButtonCallbackData(chatId, scenarioId, button.getActionId()))
                        .build();
                buttonRow.add(keyboardButton);
            }
            inlineButtons.add(buttonRow);
        }

        return new InlineKeyboardMarkup(inlineButtons);
    }
}
