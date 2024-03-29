package com.example.onboardingservice.scenaries.service;

import com.example.onboardingservice.jooq.tables.records.ScenarioRecord;
import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.impl.*;
import com.example.onboardingservice.utils.JooqUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.SelectWhereStep;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static com.example.onboardingservice.jooq.Tables.SCENARIO;
import static com.example.onboardingservice.jooq.Tables.SCENARIO_ROUTE_DEFINITION;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenariosServiceImpl implements ScenarioService {

    private final DSLContext jooq;

    @Override
    public ActionContext buildContext(Long chatId) {
        return ActionContextImpl.of(chatId);
    }

    @NotNull
    @Override
    public ScenariosMetadata initializeScenarios(ActionContext context) {
        for (var routeDescription : getRouteDescriptions()) {
            var meta = initializeScenariosWithDescription(context, routeDescription);
            if (meta != null) {
                return meta;
            }
        }
        throw new IllegalStateException("Не удалось инициализировать сценарий");
    }

    @Nullable
    @Override
    public ScenariosMetadata findActiveScenariosMetadata(Long chatId) {
        return findScenariosMetadata(it -> it.where(SCENARIO.CHAT_ID.eq(chatId).and(SCENARIO.IS_ACTIVE)));
    }

    @Override
    @NotNull
    public ActionContext findActiveContext(Long chatId) {
        return jooq.select(SCENARIO.CONTEXT).from(SCENARIO)
                .where(SCENARIO.CHAT_ID.eq(chatId).and(SCENARIO.IS_ACTIVE))
                .fetchOptional()
                .map(it -> JooqUtils.fromJsonb(it.get(SCENARIO.CONTEXT), new TypeReference<ActionContextImpl>() {}))
                .orElseThrow();
    }

    @Override
    public void saveScenariosMetadata(ActionContext context, ScenariosMetadata metadata) {
        jooq.insertInto(SCENARIO)
                .set(SCENARIO.ID, metadata.getScenarioId())
                .set(SCENARIO.SCENARIO_NAME, metadata.getScenarioName())
                .set(SCENARIO.CHAT_ID, context.getChatId())
                .set(SCENARIO.FIRST_ACTION_ID, metadata.getRoute().getFirstActionId())
                .set(SCENARIO.CURRENT_ACTION_ID, context.get(ContextConstants.ACTION_ID))
                .set(SCENARIO.CONTEXT, JooqUtils.toJsonb(context))
                .set(SCENARIO.IS_ACTIVE, true)
                .onDuplicateKeyUpdate()
                .set(SCENARIO.CURRENT_ACTION_ID, context.get(ContextConstants.ACTION_ID))
                .set(SCENARIO.CONTEXT, JooqUtils.toJsonb(context))
                .set(SCENARIO.IS_ACTIVE, true)
                .execute();
    }

    @Override
    public ScenariosMetadata changeScenarios(ActionContext context, ScenariosMetadata metadata) {
        jooq.update(SCENARIO)
                .set(SCENARIO.CURRENT_ACTION_ID, context.get(ContextConstants.ACTION_ID))
                .set(SCENARIO.CONTEXT, JooqUtils.toJsonb(context))
                .set(SCENARIO.IS_ACTIVE, false)
                .where(SCENARIO.ID.eq(metadata.getScenarioId()))
                .execute();

        var newScenariosMetadata = findScenariosMetadata(it -> it.where(SCENARIO.CHAT_ID.eq(context.getChatId())
                .and(SCENARIO.SCENARIO_NAME.eq(context.get(ContextConstants.SCENARIOS_NAME)))));

        if (newScenariosMetadata == null) {
            newScenariosMetadata = initializeScenarios(context);
        } else if (Optional.ofNullable(context.get(ContextConstants.START_FROM_BEGIN)).orElse(false)) {
            var route = newScenariosMetadata.getRoute();
            route.next(route.getFirstActionId());
        }

        return newScenariosMetadata;
    }

    private ScenariosMetadata findScenariosMetadata(
            Function<SelectWhereStep<ScenarioRecord>, SelectConditionStep<ScenarioRecord>> whereFunction) {
        var scenariosOpt = whereFunction.apply(jooq.selectFrom(SCENARIO)).fetchOptional();
        if (scenariosOpt.isEmpty()) {
            return null;
        }

        var scenarios = scenariosOpt.get();
        var record = jooq.select(SCENARIO_ROUTE_DEFINITION.ROUTE_SOURCE, SCENARIO_ROUTE_DEFINITION.FIRST_ACTION_ID)
                .from(SCENARIO_ROUTE_DEFINITION)
                .where(SCENARIO_ROUTE_DEFINITION.SCENARIO_NAME.eq(scenarios.getScenarioName()))
                .fetchOptional().orElseThrow();

        var routeBlueprint = JooqUtils.fromJsonb(record.get(SCENARIO_ROUTE_DEFINITION.ROUTE_SOURCE), new TypeReference<ScenariosRouteBlueprint>() {
        });
        var route = ScenariosRouteImpl.builder()
                .withFirstActionId(record.get(SCENARIO_ROUTE_DEFINITION.FIRST_ACTION_ID))
                .withActions(routeBlueprint.getActions())
                .withCurrentActionId(scenarios.getCurrentActionId())
                .build();

        return new ScenariosMetadataImpl(scenarios.getId(), scenarios.getScenarioName(), route);
    }

    private ScenariosMetadata initializeScenariosWithDescription(ActionContext context, ScenariosRouteDescription routeDescription) {
        var route = routeDescription.build(context);
        if (route == null) {
            return null;
        }
        return new ScenariosMetadataImpl(UUID.randomUUID(), routeDescription.getName(), route);
    }

    private List<ScenariosRouteDescription> getRouteDescriptions() {
        return jooq.selectFrom(SCENARIO_ROUTE_DEFINITION)
                .fetch()
                .stream()
                .map(it -> {
                    var routeDescription = new ScenariosRouteDescriptionImpl();
                    routeDescription.setName(it.getScenarioName());
                    routeDescription.setFirstActionId(it.getFirstActionId());
                    routeDescription.setMatchers(JooqUtils.fromJsonb(it.getMatcher(), new TypeReference<>() {
                    }));
                    routeDescription.setRoute(JooqUtils.fromJsonb(it.getRouteSource(), new TypeReference<>() {
                    }));
                    return (ScenariosRouteDescription) routeDescription;
                }).toList();
    }
}
