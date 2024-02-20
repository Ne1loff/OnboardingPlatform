package com.example.onboardingservice.handler;

import com.example.onboardingservice.messages.KeyboardFactory;
import com.example.onboardingservice.model.UserState;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.Map;

public class ResponseHandler {
    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;

    public ResponseHandler(SilentSender sender, DBContext dbContext) {
        this.sender = sender;
        chatStates = dbContext.getMap("chatStates");
    }

    public void replyToStart(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет, помощник компании Кейсистемс. Я помогаю новым сотрудникам легче освоится.\n\nПодскажи пожалуйста, как тебя зовут?");

        sender.execute(message);
        chatStates.put(chatId, UserState.AWAITING_NAME);
    }

    public void replyToButtons(long chatId, Message message) {
        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
        }

        switch (chatStates.get(chatId)) {
            case UserState.AWAITING_NAME -> replyToName(chatId, message);
            case UserState.MAIN_MENU_SELECTION -> replyToMainMenuSelection(chatId, message);
            case UserState.DOCUMENTS_SELECTION -> replyToDocumentsSelection(chatId, message);
            case UserState.AWAITING_QUESTION -> replyToQuestion(chatId, message);
            default -> unexpectedMessage(chatId);
        }
    }

    private void replyToDocumentsSelection(long chatId, Message message) {
        String text = switch (message.getText()) {
            case "Пропуск" -> "1. Паспорт\n2. Снилс";
            case "Техника" -> "1. Паспорт\n2. Подтверждение";
            case "Отпуск" -> "1. Паспорт\n2. Заявление";
            default -> "Не могу подсказать, сори";
        };

        promptWithKeyboardForState(chatId, text, KeyboardFactory.mainMenuKeyboard(), UserState.MAIN_MENU_SELECTION);
    }

    private void unexpectedMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("I did not expect that.");
        sender.execute(sendMessage);
    }

    private void stopChat(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Thank you for your order. See you soon!\nPress /start to order again");
        chatStates.remove(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sender.execute(sendMessage);
    }

    private void promptWithKeyboardForState(long chatId, String text, ReplyKeyboard YesOrNo, UserState awaitingReorder) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(YesOrNo);

        sender.execute(sendMessage);
        chatStates.put(chatId, awaitingReorder);
    }

    private void replyToMainMenuSelection(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        switch (message.getText()) {
            case "Помощь" -> {
                sendMessage.setText("Задайте вопрос, постараюсь на него ответить)");
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

                chatStates.put(chatId, UserState.AWAITING_QUESTION);
            }

            case "Документы" -> {
                sendMessage.setText("Что вы хотите оформить?");
                sendMessage.setReplyMarkup(KeyboardFactory.documentsKeyboard());

                chatStates.put(chatId, UserState.DOCUMENTS_SELECTION);
            }

            case "Контакты HR" -> sendMessage.setText("Контакты HR-ов: HR1, HR2");
            case "Как пройти" -> sendMessage.setText("Данный метод пока не реализован");
        }
        sender.execute(sendMessage);
    }

    private void replyToQuestion(long chatId, Message message) {
        promptWithKeyboardForState(chatId, "На данный момент, я не могу ответить на этот вопрос, пересылаю его HR-у",
                KeyboardFactory.mainMenuKeyboard(), UserState.MAIN_MENU_SELECTION);
    }

    private void replyToName(long chatId, Message message) {
        promptWithKeyboardForState(chatId, "Привет " + message.getText() + ", приятно познакомится!\nМожет я могу чем-то помочь?",
                KeyboardFactory.mainMenuKeyboard(),
                UserState.MAIN_MENU_SELECTION);
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}
