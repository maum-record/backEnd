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

import java.nio.file.AccessDeniedException;
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

    //유저 문의 작성
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
    //전체 문의 리스트
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
    //특정 문의 조회
    public Map<UserInquiry, AdminAnswer> findMyInquiry(Authentication authentication, Long id) throws AccessDeniedException {
        User user= userDetailService.loadUserByUsername(authentication.getName());
        UserInquiry inquiry = userInquiryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 문의를 찾지 못했습니다."));
        if(!inquiry.getUser().equals(user) && !user.getRole().equals(User.Role.ADMIN))
            throw new AccessDeniedException("권한이 없는 접근입니다.");
        AdminAnswer answer = adminAnswerRepository.findMyInquiry(inquiry).orElse(null);
        Map<UserInquiry, AdminAnswer> result = new HashMap<>();
        result.put(inquiry, answer);
        return result;
    }
    //답변안된 문의내역
    public List<UserInquiry> noReplyInquiries() {
        return userInquiryRepository.findByReplyIsNull();
    }
    //전체 답변 내역
    public List<AdminAnswer> answers() {
        return adminAnswerRepository.findAll();
    }
    //답변 작성
    public void replyAnswer(InquiryRequest request, Long inquiryId) {
        UserInquiry inquiry = userInquiryRepository.findById(inquiryId).orElseThrow(()->new IllegalArgumentException("해당 문의를 찾지 못했습니다."));
        AdminAnswer answer=adminAnswerRepository.save(AdminAnswer.builder()
                .inquiry(inquiry)
                .title("Re: "+inquiry.getTitle())
                .content(request.getMessage())
                .build());
        inquiry.setReply(answer);
        userInquiryRepository.save(inquiry);
    }
    //특정 답변 및 해당 문의 확인
    public Map<UserInquiry, AdminAnswer> answerMap(Long answerId){
        AdminAnswer answer = adminAnswerRepository.findById(answerId).orElseThrow(()-> new IllegalArgumentException("해당 답변을 찾지 못했습니다."));
        UserInquiry inquiry = answer.getInquiry();
        HashMap<UserInquiry, AdminAnswer> result = new HashMap<>();
        result.put(inquiry, answer);
        return result;
    }
}
