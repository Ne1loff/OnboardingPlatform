package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.scenaries.actions.Action;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ScenarioService {

    private final DSLContext jooq;

    public Optional<Context> findActiveScenarioContext(Long chatId) {
        return
    }

    public Optional<Scenario> findScenarioByEntryEvent(String event) {

    }

    public Context createScenarioContext(Long chatId, Scenario scenario) {

    }

    public Action getAction(UUID id, Context context) {

    }
}
