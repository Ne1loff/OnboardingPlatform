package com.example.onboardingservice.messages;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@UtilityClass
public class KeyboardFactory {

    public ReplyKeyboard mainMenuKeyboard() {
        var row1 = new KeyboardRow();
        var row2 = new KeyboardRow();

        row1.add("Помощь");
        row1.add("Контакты HR");
        row2.add("Документы");
        row2.add("Как пройти");

        return new ReplyKeyboardMarkup(List.of(row1, row2));
    }

    public ReplyKeyboard documentsKeyboard() {
        var row = new KeyboardRow();

        row.add("Пропуск");
        row.add("Отпуск");
        row.add("Техника");

        return new ReplyKeyboardMarkup(List.of(row));
    }

}
