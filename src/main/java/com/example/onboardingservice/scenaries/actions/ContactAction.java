package com.example.onboardingservice.scenaries.actions;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.AbstractAction;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
public final class ContactAction extends AbstractAction {

    private Long userId;

    @Override
    public void onUpdate(AbsSender sender, Update update, ActionContext context, ScenariosMetadata metadata) throws TelegramApiException {
        final Long chatId = context.getChatId();
        final String scenarioId = metadata.getScenarioName();

        var message = new SendContact(chatId.toString(), "88003553535", "Test" + userId);
        sender.execute(message);
    }
}
