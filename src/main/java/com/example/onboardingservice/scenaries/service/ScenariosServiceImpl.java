package com.example.onboardingservice.scenaries.service;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.impl.ActionContextImpl;
import com.example.onboardingservice.scenaries.model.impl.ScenariosMetadataImpl;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteDescriptionImpl;
import com.example.onboardingservice.utils.JooqUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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
        return jooq.selectFrom(SCENARIO)
                .where(SCENARIO.CHAT_ID.eq(chatId).and(SCENARIO.IS_ACTIVE))
                .fetchOneInto(ScenariosMetadata.class);
    }

    @Override
    public void saveScenariosMetadata(ActionContext context, ScenariosMetadata metadata) {
        jooq.insertInto(SCENARIO)
                .set(SCENARIO.ID, metadata.getScenarioId())
                .set(SCENARIO.SCENARIO_NAME, metadata.getScenarioName())
                .set(SCENARIO.CHAT_ID, context.getChatId())
                .set(SCENARIO.CURRENT_ACTION_ID, (UUID) context.getParameters().get(ContextConstants.ACTION_ID))
                .set(SCENARIO.IS_ACTIVE, true)
                .onDuplicateKeyUpdate()
                .set(SCENARIO.CURRENT_ACTION_ID, (UUID) context.getParameters().get(ContextConstants.ACTION_ID))
                .execute();

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
                    routeDescription.setMatchers(JooqUtils.fromJsonb(it.getMatcher(), new TypeReference<>() {}));
                    routeDescription.setRoute(JooqUtils.fromJsonb(it.getRouteSource(), new TypeReference<>() {}));
                    return (ScenariosRouteDescription) routeDescription;
                }).toList();
    }
}
