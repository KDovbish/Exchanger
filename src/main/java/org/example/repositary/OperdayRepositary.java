package org.example.repositary;

import org.example.entity.OperdayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Репозитарий для таблицы "Операционный день"(operday) */
@Repository
public interface OperdayRepositary extends JpaRepository<OperdayEntity, Integer> {
}
