package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.model.RouteMatcher;
import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStartEventType;
import lombok.Data;

@Data
public class RouteMatcherImpl implements RouteMatcher {

    private ScenariosStartEventType type;
    private String value;

}
