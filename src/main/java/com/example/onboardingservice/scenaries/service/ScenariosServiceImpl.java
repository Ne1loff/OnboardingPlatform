package com.example.onboardingservice.scenaries.service;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.Action;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.impl.ActionContextImpl;
import com.example.onboardingservice.scenaries.model.impl.ScenariosMetadataImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

import static com.example.onboardingservice.jooq.Tables.SCENARIO_ROUTE_DEFINITION;

@Slf4j
@RequiredArgsConstructor
public class ScenariosServiceImpl implements ScenarioService {

    private final DSLContext jooq;

    @Override
    public ActionContext buildContext(Long chatId) {
        return ActionContextImpl.of(chatId);
    }

    @Override
    public ScenariosMetadata initializeScenarios(ActionContext context) {


        return new ScenariosMetadataImpl(UUID.randomUUID(), null);
    }

    @Override
    public Action buildCurrentAction(ScenariosMetadata metadata, ActionContext context) {
        return null;
    }

    @Override
    public ScenariosMetadata findActiveScenariosMetadata(Long chatId) {
        return null;
    }

    private List<ScenariosRouteDescription> getRouteDescriptions() {
        return jooq.selectFrom(SCENARIO_ROUTE_DEFINITION)
                .fetchInto(ScenariosRouteDescription.class);
    }
}
