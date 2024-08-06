package com.example.onboardingservice.controllers;

import com.example.onboardingservice.model.HrDto;
import com.example.onboardingservice.service.HrsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hrs")
public class HrsController {

    private final HrsService hrsService;

    @GetMapping
    public ResponseEntity<List<HrDto>> getHrs() {
        return ResponseEntity.ok(hrsService.getHrs());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteHrs(@RequestBody List<Long> hrChatIds) {
        hrsService.deleteHrs(hrChatIds);
        return ResponseEntity.ok().build();
    }
}
