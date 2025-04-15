package maumrecord.maumrecord.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "healing_program")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 프로그램 이름
    @Column(nullable = false)
    private String title;

    // 간단한 설명
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // 카테고리 (예: 명상, 음악, 호흡 등)
    @Column
    private String category;

    // 썸네일 이미지 경로
    @Column
    private String imageUrl;

    // 삭제 여부 (논리 삭제 처리용)
    @Column(nullable = false)
    private Boolean isDeleted = false;
}
