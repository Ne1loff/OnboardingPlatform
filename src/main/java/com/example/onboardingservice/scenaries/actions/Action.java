package com.example.onboardingservice.scenaries.actions;

import com.example.onboardingservice.scenaries.actions.impl.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @Type(value = ChangeScenariosAction.class),
        @Type(value = ForwardMessageAction.class),
        @Type(value = ReadMessageAction.class),
        @Type(value = SendContactAction.class),
        @Type(value = SendFileAction.class),
        @Type(value = SendMessageAction.class),
})
public interface Action extends Serializable {

    UUID getId();

    String getName();

    Optional<UUID> getNextActionId();
}
