package com.example.onboardingservice.scenaries.controller;

import com.example.onboardingservice.scenaries.ScenariosManager;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteDescriptionImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scenarios")
public class ScenariosController {

    private final ScenariosManager scenariosManager;

    @PostMapping
    public ResponseEntity<Void> createScenarios(@RequestBody ScenariosRouteDescriptionImpl scenarios) {
        scenariosManager.createScenarios(scenarios);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteScenarios(@RequestParam String scenariosName) {
        scenariosManager.deleteScenarios(scenariosName);
        return ResponseEntity.ok().build();
    }

}
