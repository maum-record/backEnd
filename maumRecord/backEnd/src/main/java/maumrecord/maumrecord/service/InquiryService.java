package maumrecord.maumrecord.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.domain.AdminAnswer;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.domain.UserInquiry;
import maumrecord.maumrecord.dto.InquiryRequest;
import maumrecord.maumrecord.repository.AdminAnswerRepository;
import maumrecord.maumrecord.repository.UserInquiryRepository;
import maumrecord.maumrecord.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryService {
    private final AdminAnswerRepository adminAnswerRepository;
    private final UserInquiryRepository userInquiryRepository;
    private final UserRepository userRepository;
    private final UserDetailService userDetailService;

    public void newInquiry(Authentication authentication, InquiryRequest request) {
        Long id=userRepository.findByEmail(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("User not found")).getId();
        userInquiryRepository.save(UserInquiry.builder()
                .title(request.getTitle())
                .id(id)
                .status(UserInquiry.InquiryStatus.PENDING)
                .message(request.getMessage())
                .build());
    }

    public Map<UserInquiry, AdminAnswer> findMyInquires(Authentication authentication) {
        User user= userDetailService.loadUserByUsername(authentication.getName());
        List<UserInquiry> inquiries = userInquiryRepository.findByUser(user);
        if(inquiries.isEmpty()) //문의내역이 비어있는 경우 빈 해시맵 반환
            return new HashMap<>();
        Map<UserInquiry, AdminAnswer> result = new LinkedHashMap<>();
        for (UserInquiry inquiry : inquiries) {
            AdminAnswer answer = adminAnswerRepository.findMyInquiry(inquiry).orElse(null);
            result.put(inquiry, answer); // answer가 없으면 null로 저장됨
        }
        return result;
    }
    //todo: 관리자 답변 부분 추후 작성
    //public void wirteAdminAnswer(Authentication authentication, InquiryRequest request) {}
}
