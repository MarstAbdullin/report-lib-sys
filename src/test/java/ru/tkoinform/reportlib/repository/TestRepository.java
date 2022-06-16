package ru.tkoinform.reportlib.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.tkoinform.reportlib.model.TestEntity;

@Component
public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
