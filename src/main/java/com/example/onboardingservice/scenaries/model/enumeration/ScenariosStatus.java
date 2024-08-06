package com.example.onboardingservice.scenaries.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScenariosStatus {
    DRAFT(true),
    TEST(true),
    PUBLISHED(false),
    ARCHIVED(false);

    private final boolean editable;
}
