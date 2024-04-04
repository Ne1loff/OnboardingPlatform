package com.example.onboardingservice.scenaries.actions.impl;

import com.example.onboardingservice.scenaries.actions.AbstractAction;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class ChangeScenariosAction extends AbstractAction {
    private String nextScenariosName;
    private boolean startFromBegin;
}
