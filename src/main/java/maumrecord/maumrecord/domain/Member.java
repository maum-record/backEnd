package maumrecord.maumrecord.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "member")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    //ToDo: 각 컬럼 길이 제한?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id", updatable = false)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;
//     Todo:프론트에서 회원가입 수정  후 적용
//    @Column(name="nickName", nullable = false)
//    private String nickName;

    @Column(name = "email", nullable = false,unique = true)
    private String email;

    //Todo: 비밀번호 저장방식
    @Column(name = "password",nullable = false)
    private String password;

    @CreatedDate
    @Column(name = "createdAt",updatable = false)
    private LocalDateTime createdAt;

    //ToDo:createdAt 형식 지정?
    @PrePersist
    public void prePersist(){
        if(this.createdAt==null)
            this.createdAt=LocalDateTime.now();
    }

    @Column(name = "journalCount")
    private Long number;

    @Column(name = "lastHealingProgram")
    private String lastHealingProgram; // e.g., '명상'

    @Column(name = "lastHealingDate")
    private String lastHealingDate;    // e.g., '2025-03-22'

    @Column(name = "active")
    private boolean active;

}
