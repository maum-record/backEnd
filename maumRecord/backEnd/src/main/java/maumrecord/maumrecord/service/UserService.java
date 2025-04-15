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

    //회원가입
    public void signUp(UserRequest request){
        validateDuplicateMember(request);
        userRepository.save(User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .nickName(request.getNickname())
                .build());
    }
    //회원가입 시 중복 확인
    private void validateDuplicateMember(UserRequest dto){
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    //회원정보 수정 후 저장
    public void updateUser(UserRequest request,String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("해당 유저 검색에 실패했습니다."));
        user.setNickName(request.getNickname());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setImage(request.getImage());
    }

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
    
    //로그인
    public String login(LoginRequest dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 틀림");
        }
        //로그인 시 리프레시 토큰 재발급
        String refreshToken=tokenProvider.generateToken(user,Duration.ofDays(1));
        user.setRefreshToken(refreshToken);

        return createNewAccessToken(refreshToken);
    }
    //email로 리프레시토큰 확인
    public String findRefreshToken(String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));
        return user.getRefreshToken();
    }
    //새 엑세스토큰 발급
    public String createNewAccessToken(String refreshToken){
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected token");
        }

        String email=tokenProvider.getClaims(refreshToken).getSubject();
        User user=findByEmail(email);

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token mismatch");
        }

        return tokenProvider.generateToken(user, Duration.ofMinutes(30));
    }
}
