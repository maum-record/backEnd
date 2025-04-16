package maumrecord.maumrecord.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_inquiry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus status;

    @OneToOne(mappedBy = "inquiry", fetch = FetchType.LAZY)
    private AdminAnswer reply;

    public enum InquiryStatus {
        PENDING, // 답변 대기 중
        ANSWERED // 답변 완료
    }
}

