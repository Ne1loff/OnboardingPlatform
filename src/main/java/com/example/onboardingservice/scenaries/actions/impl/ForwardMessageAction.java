package com.example.onboardingservice.scenaries.actions.impl;

import com.example.onboardingservice.scenaries.actions.AbstractAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForwardMessageAction extends AbstractAction {
    private Long forwardChatId;
}
