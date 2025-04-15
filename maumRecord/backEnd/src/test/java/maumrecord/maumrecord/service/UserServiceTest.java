package maumrecord.maumrecord.service;

import io.jsonwebtoken.Jwts;
import maumrecord.maumrecord.config.jwt.TokenProvider;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.dto.LoginRequest;
import maumrecord.maumrecord.dto.UserRequest;
import maumrecord.maumrecord.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private UserDetailService userDetailService;
    @InjectMocks
    private UserService userService;

    //회원가입 테스트
    @Test
    void signUp_ValidRequest_SavesUser() {
        // given
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPw");

        // when
        userService.signUp(request);

        // then
        verify(userRepository).save(any(User.class));
    }
    //회원가입 시 중복 테스트
    @Test
    void signUp_DuplicateEmail_ThrowsException() {
        // given
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(User.builder().email("test@example.com").password("password").build()));

        // when & then
        assertThrows(IllegalStateException.class, () -> userService.signUp(request));
    }

    //로그인 시 토큰 발급확인 테스트
    @Test
    void login_ValidCredentials_ReturnsToken() {
        // given
        String email = "test@example.com";
        String pw = "pw123";
        User user = User.builder().email(email).password("encodedPw").build();
        LoginRequest dto = new LoginRequest();
        dto.setEmail(email);
        dto.setPassword(pw);
        String testRefreshToken = "refresh-token";
        String testAccessToken = "access-token";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenProvider.generateToken(user, Duration.ofDays(1))).thenReturn(testRefreshToken); // refreshToken
        when(passwordEncoder.matches(pw, "encodedPw")).thenReturn(true);    //비밀번호 일치시킴
        when(tokenProvider.validToken(testRefreshToken)).thenReturn(true);
        when(tokenProvider.getClaims(testRefreshToken)).thenReturn(Jwts.claims().setSubject(email));  // JWT 클레임 mock
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user)); // 내부에서 다시 user 조회
        when(tokenProvider.generateToken(user, Duration.ofMinutes(30))).thenReturn(testAccessToken); // accessToken


        // when
        String resultToken = userService.login(dto);

        // then
        assertEquals(testAccessToken, resultToken);
    }

    //비밀번호 틀릴 시 테스트
    @Test
    void login_WrongPassword_ThrowsException() {
        // given
        LoginRequest dto = new LoginRequest();
        dto.setEmail("test@example.com");
        dto.setPassword("wrongPw");
        User user = User.builder().email(dto.getEmail()).password("encodedPw").build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPw", "encodedPw")).thenReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> userService.login(dto));
    }
    //잘못된 이메일로 로그인 시 테스트
    @Test
    void login_NonExistingEmail_ThrowsException() {
        // given
        LoginRequest dto = new LoginRequest();
        dto.setEmail("nonexistent@example.com");
        dto.setPassword("password");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> userService.login(dto));
    }

    //사용자의 회원탈퇴 테스트(인증 기반)
    @Test
    void deleteUser_ByAuthentication_DeletesUser() {
        // given
        User user = User.builder().email("a@a.com").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userDetailService.loadUserByUsername(user.getEmail())).thenReturn(user);

        // when
        userService.deleteUser(authentication);

        // then
        verify(userRepository).deleteById(user.getId());
    }

    //refreshToken의 사용자가 존재하지 않는 경우
    @Test
    void findRefreshToken_UserNotFound_ThrowsException() {
        // given
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> userService.findRefreshToken(email));
    }
    //token 발급 테스트
    @Test
    void createNewAccessToken_ValidRefreshToken_ReturnsAccessToken() {
        //given
        String refreshToken = "valid-refresh-token";
        String email = "test@example.com";
        User user = mock(User.class);
        String expectedAccessToken = "new-access-token";

        when(tokenProvider.validToken(refreshToken)).thenReturn(true);
        when(tokenProvider.getClaims(refreshToken)).thenReturn(Jwts.claims().setSubject(email));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userService.findRefreshToken(email)).thenReturn(refreshToken);
        when(tokenProvider.generateToken(user, Duration.ofMinutes(30))).thenReturn(expectedAccessToken);

        //when
        String result = userService.createNewAccessToken(refreshToken);

        //then
        assertEquals(expectedAccessToken, result);
    }

    //토큰값이 다른 경우
    @Test
    void createNewAccessToken_InvalidToken_ThrowsException() {
        // Given
        String invalidToken = "Unexpected token";
        when(tokenProvider.validToken(invalidToken)).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> userService.createNewAccessToken(invalidToken));
    }

    //refresh 토큰이 일치하지 않는 경우 테스트
    @Test
    void createNewAccessToken_RefreshTokenMismatch_ThrowsException() {
        // Given
        String refreshToken = "valid-refresh-token";
        String email = "test@example.com";
        User user = mock(User.class);

        when(tokenProvider.validToken(refreshToken)).thenReturn(true);
        when(tokenProvider.getClaims(refreshToken)).thenReturn(Jwts.claims().setSubject(email));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(user.getRefreshToken()).thenReturn("Refresh Token mismatch");

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> userService.createNewAccessToken(refreshToken));
    }
    //유저 업데이트 요청 시 유저 정보 반환여부 테스트
    @Test
    void update_Request_test(){
        //given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        User user = User.builder().email("test@example.com").build();
        when(userDetailService.loadUserByUsername("test@example.com")).thenReturn(user);

        //when
        User loadUser= userDetailService.loadUserByUsername(authentication.getName());

        //then
        assertEquals(user, loadUser);
    }
    //유저 업데이트 내용 저장 여부 테스트
    @Test
    void update_User_test(){
        //given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        User user = User.builder().email("test@example.com").nickName("test").build();
        when(userDetailService.loadUserByUsername("test@example.com")).thenReturn(user);

        //when
        User updateUser = userDetailService.loadUserByUsername(authentication.getName());
        updateUser.setNickName("change");

        //then
        assertEquals(user.getNickName(), updateUser.getNickName());
    }
}