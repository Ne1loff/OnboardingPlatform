package com.example.onboardingservice.scenaries.controller;

import com.example.onboardingservice.scenaries.ScenariosManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scenarios")
public class ScenariosController {

    private final ScenariosManager scenariosManager;

    @PostMapping
    public RequestEntity<Void> createScenarios() {
        return null;
    }

}
