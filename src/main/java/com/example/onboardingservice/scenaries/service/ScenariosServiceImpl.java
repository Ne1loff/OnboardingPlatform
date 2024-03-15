package com.example.onboardingservice.scenaries.service;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.Action;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.impl.ActionContextImpl;
import com.example.onboardingservice.scenaries.model.impl.ScenariosMetadataImpl;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteDescriptionImpl;
import com.example.onboardingservice.utils.JooqUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

import static com.example.onboardingservice.jooq.Tables.SCENARIO;
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
        for (var routeDescription : getRouteDescriptions()) {
            var meta = initializeScenariosWithDescription(context, routeDescription);
            if (meta != null) {
                return meta;
            }
        }
        throw new IllegalStateException("Не удалось инициализировать сценарий");
    }

    @Override
    public Action buildCurrentAction(ScenariosMetadata metadata, ActionContext context) {
        return metadata.getRoute().current();
    }

    @Override
    public ScenariosMetadata findActiveScenariosMetadata(Long chatId) {
        return jooq.selectFrom(SCENARIO)
                .where(SCENARIO.CHAT_ID.eq(chatId).and(SCENARIO.IS_ACTIVE))
                .fetchOneInto(ScenariosMetadata.class);
    }

    private ScenariosMetadata initializeScenariosWithDescription(ActionContext context, ScenariosRouteDescription routeDescription) {
        var route = routeDescription.build(context);
        if (route == null) {
            return null;
        }
        return new ScenariosMetadataImpl(UUID.randomUUID(), route);
    }

    private List<ScenariosRouteDescription> getRouteDescriptions() {
        return jooq.selectFrom(SCENARIO_ROUTE_DEFINITION)
                .fetch()
                .stream()
                .map(it -> {
                    var routeDescription = new ScenariosRouteDescriptionImpl();
                    routeDescription.setName(it.getScenarioName());
                    routeDescription.setFirstActionId(it.getFirstActionId());
                    routeDescription.setMatchers(JooqUtils.fromJsonb(it.getMatcher()));
                    routeDescription.setRoute(JooqUtils.fromJsonb(it.getRouteSource()));
                    return (ScenariosRouteDescription) routeDescription;
                }).toList();
    }
}
