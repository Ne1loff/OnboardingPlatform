package com.example.onboardingservice.scenaries.actions;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Getter
@Setter
public final class ChangeScenariosAction implements Action {

    private String name;
    private UUID id;
    private UUID nextActionId;
    private String nextScenariosName;
    private boolean startFromBegin;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<UUID> getNextActionId() {
        return Optional.ofNullable(nextActionId);
    }

    @Override
    public void onUpdate(AbsSender sender, Update update, ActionContext context, ScenariosMetadata metadata) throws TelegramApiException {
        log.info("In [{}] scenarios will change to ({})", ChangeScenariosAction.class.getSimpleName(), nextScenariosName);
        context.put(ContextConstants.SCENARIOS_NAME, nextScenariosName);
        context.put(ContextConstants.NEED_INIT, true);
        context.put(ContextConstants.START_FROM_BEGIN, startFromBegin);
    }


}
