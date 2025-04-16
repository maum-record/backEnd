package maumrecord.maumrecord.repository;

import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.domain.UserInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInquiryRepository extends JpaRepository<UserInquiry, Long> {
    List<UserInquiry> findByUser(User user);
    List<UserInquiry> findByReplyIsNull();
}
