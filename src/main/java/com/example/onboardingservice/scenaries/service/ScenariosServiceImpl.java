package com.example.onboardingservice.scenaries.service;

import com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition;
import com.example.onboardingservice.jooq.tables.records.ScenarioRecord;
import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStatus;
import com.example.onboardingservice.scenaries.model.impl.*;
import com.example.onboardingservice.utils.JooqUtils;
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
        return ActionContext.of(chatId);
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
    public ScenariosMetadata findActiveScenariosMetadata(ActionContext context) {
        var scenarios = findScenariosMetadata(it -> it.where(SCENARIO.CHAT_ID.eq(context.getChatId())
                .and(SCENARIO.IS_ACTIVE)));

        if (scenarios == null) {
            return null;
        }

        try {
            var newScenarios = initializeScenarios(context);
            var newContext = ActionContext.of(context.getChatId());
            newContext.setActionId(newScenarios.getRoute().getFirstActionId());

            context.setNextScenarioRouteDefinitionId(newScenarios.getScenarioId());
            context.setStartFromBegin(true);
            context.restore(findActiveContext(context.getChatId()));

            saveScenariosMetadata(newContext, newScenarios);
            return changeScenarios(context, scenarios);
        } catch (IllegalStateException ignore) {
            // нет запроса нового сценария
        }

        var definition = jooq.selectFrom(SCENARIO_ROUTE_DEFINITION)
                .where(SCENARIO_ROUTE_DEFINITION.ID.eq(scenarios.getRouteId()))
                .fetchOptionalInto(ScenarioRouteDefinition.class)
                .orElseThrow();

        var includeTest = context.isIncludeTestScenarios();
        return switch (ScenariosStatus.valueOf(definition.getStatus())) {
            case TEST -> includeTest ? scenarios : null;
            case PUBLISHED -> scenarios;
            case DRAFT, ARCHIVED -> null;
        };
    }

    @Override
    public ActionContext findActiveContext(Long chatId) {
        return jooq.select(SCENARIO.CONTEXT).from(SCENARIO)
                .where(SCENARIO.CHAT_ID.eq(chatId).and(SCENARIO.IS_ACTIVE))
                .fetchOptional()
                .map(it -> JooqUtils.fromJsonb(it.get(SCENARIO.CONTEXT), ActionContext.class))
                .orElse(null);
    }

    @Override
    public void saveScenariosMetadata(ActionContext context, ScenariosMetadata metadata) {
        jooq.insertInto(SCENARIO)
                .set(SCENARIO.ID, metadata.getScenarioId())
                .set(SCENARIO.SCENARIO_NAME, metadata.getScenarioName())
                .set(SCENARIO.ROUTE_DEFINITION_ID, metadata.getRouteId())
                .set(SCENARIO.CHAT_ID, context.getChatId())
                .set(SCENARIO.FIRST_ACTION_ID, metadata.getRoute().getFirstActionId())
                .set(SCENARIO.CURRENT_ACTION_ID, context.getActionId())
                .set(SCENARIO.CONTEXT, JooqUtils.toJsonb(context))
                .set(SCENARIO.IS_ACTIVE, true)
                .onDuplicateKeyUpdate()
                .set(SCENARIO.CURRENT_ACTION_ID, context.getActionId())
                .set(SCENARIO.CONTEXT, JooqUtils.toJsonb(context))
                .set(SCENARIO.IS_ACTIVE, true)
                .execute();
    }

    @Override
    public ScenariosMetadata changeScenarios(ActionContext context, ScenariosMetadata metadata) {
        jooq.update(SCENARIO)
                .set(SCENARIO.CURRENT_ACTION_ID, context.getActionId())
                .set(SCENARIO.CONTEXT, JooqUtils.toJsonb(context))
                .set(SCENARIO.IS_ACTIVE, false)
                .where(SCENARIO.ID.eq(metadata.getScenarioId()))
                .execute();

        var newScenariosMetadata = findScenariosMetadata(it -> it.where(SCENARIO.CHAT_ID.eq(context.getChatId())
                .and(SCENARIO.ID.eq(context.getNextScenarioRouteDefinitionId()))));

        if (newScenariosMetadata == null) {
            newScenariosMetadata = initializeScenarios(context);
        } else if (Optional.ofNullable(context.getStartFromBegin()).orElse(false)) {
            var route = newScenariosMetadata.getRoute();
            route.next(route.getFirstActionId());
        }

        jooq.update(SCENARIO)
                .set(SCENARIO.IS_ACTIVE, true)
                .where(SCENARIO.ID.eq(newScenariosMetadata.getScenarioId()))
                .execute();

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

        var routeBlueprint = JooqUtils.fromJsonb(record.get(SCENARIO_ROUTE_DEFINITION.ROUTE_SOURCE), ScenariosRouteBlueprint.class);
        var route = ScenariosRouteImpl.builder()
                .withFirstActionId(record.get(SCENARIO_ROUTE_DEFINITION.FIRST_ACTION_ID))
                .withActions(routeBlueprint.getActions())
                .withCurrentActionId(scenarios.getCurrentActionId())
                .build();

        return new ScenariosMetadataImpl(scenarios.getId(), scenarios.getScenarioName(), scenarios.getRouteDefinitionId(), route);
    }

    private ScenariosMetadata initializeScenariosWithDescription(ActionContext context, ScenariosRouteDescription routeDescription) {
        var route = routeDescription.build(context);
        if (route == null) {
            return null;
        }
        return new ScenariosMetadataImpl(UUID.randomUUID(), routeDescription.getName(), routeDescription.getId(), route);
    }

    private List<ScenariosRouteDescription> getRouteDescriptions() {
        var statuses = List.of(ScenariosStatus.PUBLISHED.toString(), ScenariosStatus.TEST.toString());
        return jooq.selectFrom(SCENARIO_ROUTE_DEFINITION)
                .where(SCENARIO_ROUTE_DEFINITION.STATUS.in(statuses))
                .fetch()
                .stream()
                .map(it -> {
                    var routeDescription = new ScenariosRouteDescriptionImpl();
                    routeDescription.setId(it.getId());
                    routeDescription.setName(it.getScenarioName());
                    routeDescription.setFirstActionId(it.getFirstActionId());
                    routeDescription.setStatus(ScenariosStatus.valueOf(it.getStatus()));
                    routeDescription.setMatcher(JooqUtils.fromJsonb(it.getMatcher(), RouteMatcher.class));
                    routeDescription.setRoute(JooqUtils.fromJsonb(it.getRouteSource(), ScenariosRouteBlueprint.class));
                    return (ScenariosRouteDescription) routeDescription;
                }).toList();
    }
}
