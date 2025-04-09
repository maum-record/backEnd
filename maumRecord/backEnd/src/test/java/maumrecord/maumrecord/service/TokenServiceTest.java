package maumrecord.maumrecord.service;

import io.jsonwebtoken.Jwts;
import maumrecord.maumrecord.config.jwt.TokenProvider;
import maumrecord.maumrecord.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private UserService userService;
    @InjectMocks
    private TokenService tokenService;

    //접근 토큰 생성 테스트
    @Test
    void createNewAccessToken_ValidRefreshToken_ReturnsAccessToken() {
        //given
        String refreshToken = "valid-refresh-token";
        String email = "test@example.com";
        User user = mock(User.class);
        String expectedAccessToken = "new-access-token";

        when(tokenProvider.validToken(refreshToken)).thenReturn(true);
        when(tokenProvider.getClaims(refreshToken)).thenReturn(Jwts.claims().setSubject(email));
        when(userService.findByEmail(email)).thenReturn(user);
        when(user.getRefreshToken()).thenReturn(refreshToken);
        when(tokenProvider.generateToken(user, Duration.ofMinutes(15))).thenReturn(expectedAccessToken);

        //when
        String result = tokenService.createNewAccessToken(refreshToken);

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
                () -> tokenService.createNewAccessToken(invalidToken));
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
        when(userService.findByEmail(email)).thenReturn(user);
        when(user.getRefreshToken()).thenReturn("Refresh Token mismatch");

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> tokenService.createNewAccessToken(refreshToken));
    }

}