package com.example.onboardingservice.service;

import com.example.onboardingservice.jooq.tables.daos.OnboardingHrsDao;
import com.example.onboardingservice.model.HrDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrsServiceImpl implements HrsService {

    private final OnboardingHrsDao onboardingHrsDao;

    @Override
    public List<HrDto> getHrs() {
        return onboardingHrsDao.findAll().stream()
                .map(it -> HrDto.of(it.getChatId(), it.getUsername(), it.getFirstName(), it.getLastName()))
                .toList();
    }

    @Override
    @Transactional
    public void deleteHrs(List<Long> hrChatIds) {
        onboardingHrsDao.deleteById(hrChatIds);
    }

    @Override
    public boolean isHrChatId(Long hrChatId) {
        return onboardingHrsDao.existsById(hrChatId);
    }
}
