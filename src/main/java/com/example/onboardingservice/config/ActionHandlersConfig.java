package com.example.onboardingservice.config;

import com.example.onboardingservice.scenaries.ScenarioService;
import com.example.onboardingservice.scenaries.actions.Action;
import com.example.onboardingservice.scenaries.handlers.ActionHandler;
import com.example.onboardingservice.scenaries.handlers.ChangeScenariosActionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActionHandlersConfig {

    @Bean
    public ActionHandler<? extends Action> changeScenarioActionHandler(ScenarioService service) {
        return new ChangeScenariosActionHandler(service);
    }

}
