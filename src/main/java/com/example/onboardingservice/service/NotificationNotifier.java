package com.example.onboardingservice.service;

import com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification;

public interface NotificationNotifier {

    void notify(ScenarioNotification notification);

}
