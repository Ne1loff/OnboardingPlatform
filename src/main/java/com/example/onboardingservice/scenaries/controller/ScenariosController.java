package com.example.onboardingservice.scenaries.controller;

import com.example.onboardingservice.model.ScenariosCreateResponse;
import com.example.onboardingservice.scenaries.ScenariosManager;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteDescriptionImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scenarios")
public class ScenariosController {

    private final ScenariosManager scenariosManager;

    @GetMapping
    public ResponseEntity<List<ScenariosRouteDescription>> getScenarios() {
        return ResponseEntity.ok(scenariosManager.getAllScenarios());
    }

    @GetMapping("/{scenarioId}")
    public ResponseEntity<ScenariosRouteDescription> getScenario(@PathVariable UUID scenarioId) {
        return ResponseEntity.ok(scenariosManager.getScenarioById(scenarioId));
    }

    @PostMapping
    public ResponseEntity<ScenariosCreateResponse> createScenarios(@RequestBody ScenariosRouteDescriptionImpl scenarios) {
        var scenariosId = scenariosManager.createScenarios(scenarios);
        return ResponseEntity.created(URI.create("/api/v1/scenarios/" + scenariosId))
                .body(ScenariosCreateResponse.of(scenariosId));
    }

    @PutMapping("/{scenarioId}")
    public ResponseEntity<ScenariosCreateResponse> updateScenario(
            @PathVariable UUID scenarioId,
            @RequestBody ScenariosRouteDescriptionImpl scenarios) {
        return ResponseEntity.ok(scenariosManager.updateScenarios(scenarioId, scenarios));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteScenarios(@RequestParam String scenariosName) {
        scenariosManager.deleteScenarios(scenariosName);
        return ResponseEntity.ok().build();
    }

}
