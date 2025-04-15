package maumrecord.maumrecord.repository;

import maumrecord.maumrecord.domain.AdminAnswer;
import maumrecord.maumrecord.domain.UserInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminAnswerRepository extends JpaRepository<AdminAnswer,Long> {
    Optional<AdminAnswer> findMyInquiry(UserInquiry userInquiry);
}
