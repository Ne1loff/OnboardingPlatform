package com.example.onboardingservice.scenaries.handlers;

import com.example.onboardingservice.scenaries.ActionContext;
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

    @Override
    public Class<ChangeScenariosAction> getHandledClass() {
        return ChangeScenariosAction.class;
    }

    @Override
    public Optional<UUID> process(ChangeScenariosAction action, AbsSender sender, Update update, ActionContext context, ScenariosMetadata metadata) throws TelegramApiException {
        context.setNextScenarioRouteDefinitionId(action.getNextScenarioRouteDefinitionId());
        context.setInitNextScenarios(true);
        context.setStartFromBegin(action.isStartFromBegin());

        return action.getNextActionId();
    }
}
