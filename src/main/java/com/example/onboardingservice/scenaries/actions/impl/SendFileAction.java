package com.example.onboardingservice.scenaries.actions.impl;

import com.example.onboardingservice.scenaries.actions.AbstractAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendFileAction extends AbstractAction {
    private String fileId;
    private String fileName;
}
