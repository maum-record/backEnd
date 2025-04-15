package maumrecord.maumrecord.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 사용자 (외래키)

    @Column(nullable = false)
    private String activityType;  // 로그 유형 (login, diary_creation, healing_program_execution 등)

    private Long targetId;  // 관련된 대상 ID (예: 일기 ID, 프로그램 ID 등)

    @Column(name = "activity_time", nullable = false)
    private LocalDateTime activityTime;  // 활동 시간

    @Column(columnDefinition = "TEXT")
    private String details;  // 상세 정보 (예: 일기 내용, 프로그램 이름 등)

}
