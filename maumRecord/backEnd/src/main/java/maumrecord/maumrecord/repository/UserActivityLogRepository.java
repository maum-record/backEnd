package maumrecord.maumrecord.repository;

import maumrecord.maumrecord.domain.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {
}
