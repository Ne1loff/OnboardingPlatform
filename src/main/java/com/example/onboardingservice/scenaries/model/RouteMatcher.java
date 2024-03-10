package com.example.onboardingservice.scenaries.model;

import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStartEventType;

public interface RouteMatcher {
    ScenariosStartEventType getType();
    String getValue();
}
