package maumrecord.maumrecord.service;

import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    UserDetailService userDetailService;

    @Test
    void loadUserByUsername_ValidEmail_ReturnsUser() {
        // given
        String email = "test@example.com";
        User mockUser = mock(User.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        // when
        User result = userDetailService.loadUserByUsername(email);
        // then
        assertEquals(mockUser, result);
    }

    @Test
    void loadUserByUsername_InvalidEmail_ThrowsException() {
        // given
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> userDetailService.loadUserByUsername(email));
    }

}