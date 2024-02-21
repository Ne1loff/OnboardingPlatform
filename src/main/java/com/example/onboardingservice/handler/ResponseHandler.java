package com.example.onboardingservice.handler;

import com.example.onboardingservice.messages.KeyboardFactory;
import com.example.onboardingservice.model.UserState;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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
        message.setText("Привет, я помощник компании \"НПЦ КСБ\" и \"КСБ-СОФТ\". Я помогаю новым сотрудникам освоиться.\n" +
                "Пожалуйста, подскажи, как тебя зовут?");

        sender.execute(message);
        chatStates.put(chatId, UserState.AWAITING_NAME);
    }

    public void replyToButtons(long chatId, Message message) {
        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
        }

        switch (chatStates.get(chatId)) {
            case UserState.AWAITING_NAME -> replyToName(chatId, message.getText());
            case UserState.MAIN_MENU_SELECTION -> replyToMainMenuSelection(chatId, message.getText());
            case UserState.DOCUMENTS_SELECTION -> replyToDocumentsSelection(chatId, message.getText());
            case UserState.AWAITING_QUESTION -> replyToQuestion(chatId, message.getText());
            default -> unexpectedMessage(chatId);
        }
    }

    public void replyToButtons(long chatId, CallbackQuery query) {

        switch (chatStates.get(chatId)) {
            case UserState.AWAITING_NAME -> replyToName(chatId, query.getData());
            case UserState.MAIN_MENU_SELECTION -> replyToMainMenuSelection(chatId, query.getData());
            case UserState.DOCUMENTS_SELECTION -> replyToDocumentsSelection(chatId, query.getData());
            case UserState.AWAITING_QUESTION -> replyToQuestion(chatId, query.getData());
            default -> unexpectedMessage(chatId);
        }
    }

    private void replyToDocumentsSelection(long chatId, String data) {
        String text = switch (data) {
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

    private void replyToMainMenuSelection(long chatId, String data) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        switch (data) {
            case "Задать вопрос" -> {
                sendMessage.setText("Задайте вопрос, постараюсь на него ответить)");

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

    private void replyToQuestion(long chatId, String data) {
        promptWithKeyboardForState(chatId, "На данный момент, я не могу ответить на этот вопрос, пересылаю его HR-у",
                KeyboardFactory.mainMenuKeyboard(), UserState.MAIN_MENU_SELECTION);
    }

    private void replyToName(long chatId, String data) {
        promptWithKeyboardForState(chatId, "Привет, " + data + ", приятно познакомится!\nМожет я могу чем-то помочь?",
                KeyboardFactory.mainMenuKeyboard(),
                UserState.MAIN_MENU_SELECTION);
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}
