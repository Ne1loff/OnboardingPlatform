package com.example.onboardingservice.scenaries.actions.impl;

import com.example.onboardingservice.scenaries.actions.AbstractAction;
import com.example.onboardingservice.scenaries.model.enumeration.NotificationMode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class ReadMessageAction extends AbstractAction {
    private String property;
    private String regex;
    private NotificationMode notificationMode;
    private Duration waitingTime;
    private String timeoutMessage;
}
