package maumrecord.maumrecord.service;

import maumrecord.maumrecord.domain.AdminAnswer;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.domain.UserInquiry;
import maumrecord.maumrecord.dto.InquiryRequest;
import maumrecord.maumrecord.repository.AdminAnswerRepository;
import maumrecord.maumrecord.repository.UserInquiryRepository;
import maumrecord.maumrecord.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {
    @Mock
    private UserInquiryRepository userInquiryRepository;
    @Mock
    private AdminAnswerRepository adminAnswerRepository;
    @Mock
    private UserDetailService userDetailService;
    @InjectMocks
    private InquiryService inquiryService;
    @Mock
    private UserRepository userRepository;

    //새 문의 테스트
    @Test
    void newInquiry() {
        //given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@test.com");
        User user = User.builder().email("test@test.com").build();
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.ofNullable(user));

        InquiryRequest request = new InquiryRequest();
        request.setTitle("test title");
        request.setMessage("test message");

        //when
        inquiryService.newInquiry(authentication, request);

        //then
        verify(userInquiryRepository).save(any(UserInquiry.class));
    }

    //내 문의 내역 테스트
    @Test
    void findMyInquires() {
        //given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@test.com");
        User user = User.builder().email("test@test.com").build();
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.ofNullable(user));
        when(userDetailService.loadUserByUsername("test@test.com")).thenReturn(user);

        InquiryRequest request = new InquiryRequest();
        request.setTitle("test title");
        request.setMessage("test message");
        inquiryService.newInquiry(authentication, request);

        //when
        Map<UserInquiry, AdminAnswer> result = inquiryService.findMyInquires(authentication);
        //then
        assertNotNull(result);
    }

    //내 문의 내역이 없는 경우 테스트
    @Test
    void findMyInquires_isEmpty() {
        //given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@test.com");
        User user = User.builder().email("test@test.com").build();
        when(userDetailService.loadUserByUsername("test@test.com")).thenReturn(user);

        //when
        Map<UserInquiry, AdminAnswer> result = inquiryService.findMyInquires(authentication);
        //then
        assertTrue(result.isEmpty());
    }
}