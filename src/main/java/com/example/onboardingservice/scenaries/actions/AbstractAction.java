package com.example.onboardingservice.scenaries.actions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public sealed abstract class AbstractAction implements Action
        permits ContactAction, TextAction {
    @Getter(AccessLevel.NONE)
    protected UUID nextActionId;
    protected String name;
    protected UUID id;
    protected List<ActionButton> buttons;

    @Override
    public Optional<UUID> getNextActionId() {
        return Optional.ofNullable(nextActionId);
    }

    protected boolean hasButtons() {
        return !buttons.isEmpty();
    }
}
