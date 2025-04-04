package maumrecord.maumrecord.repository;

import jakarta.persistence.EntityManager;
import maumrecord.maumrecord.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
