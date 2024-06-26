package com.example.onboardingservice.scenaries.handlers;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.impl.SendFileAction;
import com.example.onboardingservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SendFileActionHandler implements ActionHandler<SendFileAction> {

    private final FileService fileService;

    @Override
    public Class<SendFileAction> getHandledClass() {
        return SendFileAction.class;
    }

    @Override
    public Optional<UUID> process(SendFileAction action,
                                  AbsSender sender,
                                  Update update,
                                  ActionContext context,
                                  ScenariosMetadata metadata) throws TelegramApiException {
        final var chatId = context.getChatId();

        var fileData = fileService.getFile(action.getFileId());
        var documentMessage = SendDocument.builder()
                .chatId(chatId)
                .document(new InputFile(fileData.getInputStream(), action.getFileName()))
                .build();

        sender.execute(documentMessage);

        return action.getNextActionId();
    }
}
