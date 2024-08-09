package org.seememes.bybit.tester.repository;

import org.seememes.bybit.tester.entity.TestDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDataRepository extends JpaRepository<TestDataEntity, Long> {
}
