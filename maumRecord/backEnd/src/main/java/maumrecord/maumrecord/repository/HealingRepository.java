package maumrecord.maumrecord.repository;

import maumrecord.maumrecord.domain.HealingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealingRepository extends JpaRepository<HealingProgram, Long> {
}
