package com.example.onboardingservice.scenaries.service;

import com.example.onboardingservice.scenaries.ScenariosManager;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.utils.JooqUtils;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.example.onboardingservice.jooq.Tables.SCENARIO_ROUTE_DEFINITION;

@Service
@RequiredArgsConstructor
public class ScenariosManagerImpl implements ScenariosManager {

    private final DSLContext jooq;

    @Override
    public void createScenarios(ScenariosRouteDescription description) {
        jooq.transaction(() -> jooq.insertInto(SCENARIO_ROUTE_DEFINITION)
                .set(SCENARIO_ROUTE_DEFINITION.SCENARIO_NAME, description.getName())
                .set(SCENARIO_ROUTE_DEFINITION.MATCHER, JooqUtils.toJsonb(description.getMatchers()))
                .set(SCENARIO_ROUTE_DEFINITION.ROUTE_SOURCE, JooqUtils.toJsonb(description.getRoute()))
                .execute());
    }

    @Override
    public void deleteScenarios(String scenariosName) {
        jooq.transaction(() -> jooq.deleteFrom(SCENARIO_ROUTE_DEFINITION)
                .where(SCENARIO_ROUTE_DEFINITION.SCENARIO_NAME.eq(scenariosName))
                .execute());
    }
}
