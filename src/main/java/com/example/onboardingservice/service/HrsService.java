package com.example.onboardingservice.service;

import com.example.onboardingservice.model.HrDto;

import java.util.List;

public interface HrsService {
    List<HrDto> getHrs();

    void deleteHrs(List<Long> hrChatIds);

    boolean isHrChatId(Long hrChatId);
}
