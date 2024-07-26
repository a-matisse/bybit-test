package org.seememes.bybit.tester.repository;

import org.seememes.bybit.tester.entity.TestingStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestingStatsRepository extends JpaRepository<TestingStatsEntity, Long> {
}
