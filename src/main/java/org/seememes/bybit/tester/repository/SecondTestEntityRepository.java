package org.seememes.bybit.tester.repository;

import org.seememes.bybit.tester.entity.SecondTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondTestEntityRepository extends JpaRepository<SecondTestEntity, Long> {
}
