package com.example.onboardingservice.scenaries.actions.impl;

import com.example.onboardingservice.scenaries.actions.AbstractButtonsAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageAction extends AbstractButtonsAction {
    private String text;
    private boolean markdownText;
}
