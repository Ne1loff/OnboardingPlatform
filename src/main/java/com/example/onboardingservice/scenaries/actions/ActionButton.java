package com.example.onboardingservice.scenaries.actions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ActionButton {
    private String text;
    private UUID actionId;
}
