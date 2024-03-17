package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStartEventType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteMatcher {
    private ScenariosStartEventType type;
    private String value;
}
