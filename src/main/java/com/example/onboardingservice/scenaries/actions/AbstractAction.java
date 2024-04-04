package com.example.onboardingservice.scenaries.actions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public abstract class AbstractAction implements Action {
    @Getter(AccessLevel.NONE)
    protected UUID nextActionId;
    protected String name;
    protected UUID id;

    @Override
    public Optional<UUID> getNextActionId() {
        return Optional.ofNullable(nextActionId);
    }
}
