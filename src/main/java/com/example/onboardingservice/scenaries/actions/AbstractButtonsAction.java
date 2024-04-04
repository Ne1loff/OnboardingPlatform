package com.example.onboardingservice.scenaries.actions;

import com.example.onboardingservice.scenaries.model.impl.ActionButton;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class AbstractButtonsAction extends AbstractAction {
    protected List<ActionButton> buttons;

    public boolean hasButtons() {
        return !buttons.isEmpty();
    }
}
