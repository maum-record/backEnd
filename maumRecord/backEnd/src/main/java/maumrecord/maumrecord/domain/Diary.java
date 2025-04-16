package maumrecord.maumrecord.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "diary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 요약 내용
    @Column(name="summary", nullable = false, columnDefinition = "TEXT")
    private String summary;

    // 3분류 감정 결과 (POSITIVE / NEGATIVE / NEUTRAL)
    @Enumerated(EnumType.STRING)
    @Column(name = "ternary_sentiment", nullable = false)
    private Sentiment ternarySentiment;

    // 7분류 감정 결과 (NEUTRAL ~ CONTEMPT)
    @Enumerated(EnumType.STRING)
    @Column(name = "seven_sentiment", nullable = false)
    private Sentiment sevenSentiment;

    // 사용한 힐링 프로그램 ID 리스트 (복수 선택 가능)
    @ElementCollection
    @CollectionTable(name = "diary_healing_programs", joinColumns = @JoinColumn(name = "diary_id"))
    @Column(name = "healing_program_id")
    private List<Long> healingProgramIds;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum Sentiment {
        POSITIVE, NEGATIVE, NEUTRAL, JOY, SADNESS, ANGER, FEAR, SURPRISE, CONTEMPT
    }
}

