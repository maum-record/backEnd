package maumrecord.maumrecord.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "Users")
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
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

    //Todo:닉네임추가
    @Builder
    public User(String email, String password, String auth, String name ){
        this.email=email;
        this.password=password;
        this.name=name;
    }

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public  String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
