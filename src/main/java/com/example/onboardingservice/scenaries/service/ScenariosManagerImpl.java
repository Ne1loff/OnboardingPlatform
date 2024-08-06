package com.example.onboardingservice.scenaries.service;

import com.example.onboardingservice.jooq.tables.daos.ScenarioRouteDefinitionDao;
import com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition;
import com.example.onboardingservice.model.ScenariosCreateResponse;
import com.example.onboardingservice.scenaries.ScenariosManager;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStatus;
import com.example.onboardingservice.scenaries.model.impl.RouteMatcher;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteBlueprint;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteDescriptionImpl;
import com.example.onboardingservice.utils.JooqUtils;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.example.onboardingservice.jooq.Tables.SCENARIO_ROUTE_DEFINITION;

@Service
@RequiredArgsConstructor
public class ScenariosManagerImpl implements ScenariosManager {

    private final DSLContext jooq;
    private final ScenarioRouteDefinitionDao routeDefinitionDao;

    @Override
    @Transactional
    public UUID createScenarios(ScenariosRouteDescription description) {
        final var scenariosId = UUID.randomUUID();
        jooq.insertInto(SCENARIO_ROUTE_DEFINITION)
                .set(SCENARIO_ROUTE_DEFINITION.ID, scenariosId)
                .set(SCENARIO_ROUTE_DEFINITION.SCENARIO_NAME, description.getName())
                .set(SCENARIO_ROUTE_DEFINITION.FIRST_ACTION_ID, description.getFirstActionId())
                .set(SCENARIO_ROUTE_DEFINITION.STATUS, description.getStatus().toString())
                .set(SCENARIO_ROUTE_DEFINITION.MATCHER, JooqUtils.toJsonb(description.getMatcher()))
                .set(SCENARIO_ROUTE_DEFINITION.ROUTE_SOURCE, JooqUtils.toJsonb(description.getRoute()))
                .execute();

        return scenariosId;
    }

    @Override
    @Transactional
    public void deleteScenarios(String scenariosName) {
        jooq.deleteFrom(SCENARIO_ROUTE_DEFINITION)
                .where(SCENARIO_ROUTE_DEFINITION.SCENARIO_NAME.eq(scenariosName))
                .execute();
    }

    @Override
    public List<ScenariosRouteDescription> getAllScenarios() {
        return routeDefinitionDao.findAll().stream()
                .map(this::mapToDescription)
                .toList();
    }

    @Override
    public ScenariosRouteDescription getScenarioById(UUID scenarioId) {
        return routeDefinitionDao.fetchOptionalById(scenarioId)
                .map(this::mapToDescription)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public ScenariosCreateResponse updateScenarios(UUID scenarioId, ScenariosRouteDescriptionImpl scenarios) {
        var oldDefinition =routeDefinitionDao.fetchOptionalById(scenarioId)
                .orElseThrow(NotFoundException::new);

        if (!ScenariosStatus.valueOf(oldDefinition.getStatus()).isEditable()) {
            throw new BadRequestException("Scenario with status [%s] is not editable".formatted(oldDefinition.getStatus()));
        }

        oldDefinition.setScenarioName(scenarios.getName());
        oldDefinition.setFirstActionId(scenarios.getFirstActionId());
        oldDefinition.setStatus(scenarios.getStatus().toString());
        oldDefinition.setMatcher(JooqUtils.toJsonb(scenarios.getMatcher()));
        oldDefinition.setRouteSource(JooqUtils.toJsonb(scenarios.getRoute()));

        routeDefinitionDao.update(oldDefinition);

        return ScenariosCreateResponse.of(oldDefinition.getId());
    }

    private ScenariosRouteDescription mapToDescription(ScenarioRouteDefinition definition) {
        var description = new ScenariosRouteDescriptionImpl();
        description.setId(definition.getId());
        description.setName(definition.getScenarioName());
        description.setFirstActionId(definition.getFirstActionId());
        description.setStatus(ScenariosStatus.valueOf(definition.getStatus()));
        description.setMatcher(JooqUtils.fromJsonb(definition.getMatcher(), RouteMatcher.class));
        description.setRoute(JooqUtils.fromJsonb(definition.getRouteSource(), ScenariosRouteBlueprint.class));

        return description;
    }
}
