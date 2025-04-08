package maumrecord.maumrecord.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.config.jwt.TokenProvider;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.dto.LoginRequest;
import maumrecord.maumrecord.dto.UserRequest;
import maumrecord.maumrecord.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final UserDetailService userDetailService;

    public void signUp(UserRequest request){
        validateDuplicateMember(request);
        userRepository.save(User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build());
    }

    private void validateDuplicateMember(UserRequest dto){
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public void updateUser(UserRequest request,String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("해당 유저 검색에 실패했습니다."));
        user.setNickName(request.getNickname());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setImage(request.getImage());
    }

    //todo: 관리자 삭제 여부 설정
    //userId로 유저탈퇴(관리자전용)
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
    //user의 인증정보로 탈퇴(일반유저전용)
    public void deleteUser(Authentication authentication){
        User user=userDetailService.loadUserByUsername(authentication.getName());
        deleteUser(user.getId());
    }

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    public List<User> findUsers(){return userRepository.findAll();}

    public String login(LoginRequest dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 틀림");
        }

        //todo: 토큰 발급 시 지속 시간 정하기(현재 2시간)
        return tokenProvider.generateToken(user, Duration.ofMinutes(30));
    }

    public String findRefreshToken(String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));
        return user.getRefreshToken();
    }

    public void setRefreshToken(String refreshToken,String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));
        user.setRefreshToken(refreshToken);

    }
}
