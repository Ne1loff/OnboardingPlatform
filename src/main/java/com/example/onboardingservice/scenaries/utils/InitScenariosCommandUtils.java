package com.example.onboardingservice.scenaries.utils;

import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStartEventType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InitScenariosCommandUtils {

    public static final String COMMAND_PREFIX = "/";
    public static final String BUTTON_PREFIX = "btn_";

    public ScenariosStartEventType parseEventType(String message) {
        if (message.startsWith(COMMAND_PREFIX)) {
            return ScenariosStartEventType.COMMAND;
        } else if (message.startsWith(BUTTON_PREFIX)) {
            return ScenariosStartEventType.BUTTON;
        } else {
            return ScenariosStartEventType.TEXT;
        }
    }

}
