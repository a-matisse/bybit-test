package org.seememes.bybit.tester.controller;

import org.seememes.bybit.tester.repository.TestingStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingStatsController {
    private final TestingStatsRepository testingStatsRepository;

    public TestingStatsController(
            @Autowired TestingStatsRepository testingStatsRepository
    ) {
        this.testingStatsRepository = testingStatsRepository;
    }

    @GetMapping("/results")
    public ResponseEntity<?> getResultList() {
        return ResponseEntity.ok(
                testingStatsRepository.findAll()
        );
    }
}
