package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.domain.AdminAnswer;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.domain.UserInquiry;
import maumrecord.maumrecord.dto.InquiryRequest;
import maumrecord.maumrecord.service.InquiryService;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "관리자 관련")
public class AdminController {
    private final UserService userService;
    private final InquiryService inquiryService;

    @GetMapping(value = "/users")
    @Operation(summary = "회원목록 조회")
    public List<User> list() {
        return userService.findUsers();
    }

    @GetMapping(value = "/users/{id}")
    @Operation(summary = "특정 회원 조회 by id")
    public  User user(@PathVariable Long id){
        return userService.findById(id);
    }

    @GetMapping(value="/delete/{id}")
    @Operation(summary = "특정 회원 삭제 by id")
    public ResponseEntity<String> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return ResponseEntity.ok("회원탈퇴 완료");
    }
    //todo: 관리자 답변 부분 정해지면 작성 - 전체 답변 리스트, 특정 답변 조회, 답변 작성, 답변 미작성 리스트
    @GetMapping(value="/noreplyInquiries")
    @Operation(summary = "답변 없는 문의 리스트 확인")
    public List<UserInquiry> noreplyInquiries(){
        return inquiryService.noReplyInquiries();
    }

    @GetMapping(value="/answers")
    @Operation(summary = "답변 리스트 확인")
    public List<AdminAnswer> adminAnswers(){
        return inquiryService.answers();
    }

    @PostMapping(value = "/replyAnswer/{inquiryId}")
    @Operation(summary = "답변 리스트 확인")
    public ResponseEntity<String> adminAnswers(InquiryRequest request, @PathVariable Long inquiryId){
        inquiryService.replyAnswer(request,inquiryId);
        return ResponseEntity.ok("답변 작성 완료");
    }

    @GetMapping(value="/answer/{id}")
    @Operation(summary = "특정 답변 확인")
    public Map<UserInquiry,AdminAnswer> answer(@PathVariable Long id){
        return inquiryService.answerMap(id);
    }
}
