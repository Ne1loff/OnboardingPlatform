package com.example.onboardingservice.scenaries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class ContextConstants {

    @Getter
    @AllArgsConstructor
    public class ContextParameter<T> {
        String key;
        Class<T> parameterClass;
    }

    public static final ContextParameter<Long> CHAT_ID = new ContextParameter<>("CHAT_ID", Long.class);
    public static final ContextParameter<String> SCENARIOS_NAME = new ContextParameter<>("SCENARIOS_NAME", String.class);
    public static final ContextParameter<UUID> ACTION_ID = new ContextParameter<>("ACTION_ID", UUID.class);
    public static final ContextParameter<Boolean> NEED_INIT = new ContextParameter<>("NEED_INIT", Boolean.class);
    public static final ContextParameter<Boolean> START_FROM_BEGIN = new ContextParameter<>("START_FROM_BEGIN", Boolean.class);

}
