package com.example.onboardingservice.scenaries.actions.impl;

import com.example.onboardingservice.scenaries.actions.AbstractAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendContactAction extends AbstractAction {
    private String phoneNumber;
    private String firstName;
    private String lastName;
}
