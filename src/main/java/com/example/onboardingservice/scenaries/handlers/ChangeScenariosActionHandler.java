package com.example.onboardingservice.scenaries.handlers;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.impl.ChangeScenariosAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChangeScenariosActionHandler implements ActionHandler<ChangeScenariosAction> {

    private final ScenarioService service;

    @Override
    public Class<ChangeScenariosAction> getHandledClass() {
        return ChangeScenariosAction.class;
    }

    @Override
    public Optional<UUID> process(ChangeScenariosAction action, AbsSender sender, Update update, ActionContext context, ScenariosMetadata metadata) throws TelegramApiException {
        context.put(ContextConstants.SCENARIOS_NAME, action.getNextScenariosName());
        context.put(ContextConstants.NEED_INIT, true);
        context.put(ContextConstants.START_FROM_BEGIN, action.isStartFromBegin());

        return action.getNextActionId();
    }
}
