package maumrecord.maumrecord.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 답변하는 문의의 id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false)
    private UserInquiry inquiry;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime answeredAt;
}

