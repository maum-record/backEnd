package maumrecord.maumrecord.repository;

import maumrecord.maumrecord.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
