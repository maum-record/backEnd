package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.domain.AdminAnswer;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.domain.UserInquiry;
import maumrecord.maumrecord.dto.InquiryRequest;
import maumrecord.maumrecord.dto.UserRequest;
import maumrecord.maumrecord.service.InquiryService;
import maumrecord.maumrecord.service.UserDetailService;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "로그인 회원 관련 기능")
public class UserController {
    private final UserService userService;
    private final InquiryService inquiryService;
    private final UserDetailService userDetailService;

    //todo: 로그아웃, 회원정보 수정, 비밀번호 저장방식?, 로그인 방식 업데이트
    @GetMapping(value="/delete")
    @Operation(summary = "회원탈퇴")
    public ResponseEntity<String> deleteUser(Authentication authentication)
    {
        userService.deleteUser(authentication);
        return ResponseEntity.ok("회원탈퇴 완료");
    }

    @GetMapping(value = "/profile")
    @Operation(summary = "회원 정보 확인")
    public User selectUser(Authentication authentication){
        return userDetailService.loadUserByUsername(authentication.getName());
    }

    @PostMapping(value = "/update")
    @Operation(summary = "회원 정보 수정")
    public ResponseEntity<String> updateUser(UserRequest request,Authentication authentication){
        userService.updateUser(request,authentication.getName());
        return ResponseEntity.ok("회원 정보 수정 완료");
    }

    @PostMapping(value = "/inquiry")
    @Operation(summary = "1대1 문의 전송")
    public ResponseEntity<String> newInquiry(InquiryRequest request, Authentication authentication){
        inquiryService.newInquiry(authentication,request);
        return ResponseEntity.ok("1대1 문의 접수 완료");
    }

    @GetMapping(value = "/my-inquiries")
    @Operation(summary = "내 문의 내역")
    public Map<UserInquiry, AdminAnswer> myInquiries(Authentication authentication){
        return inquiryService.findMyInquires(authentication);
    }

    @GetMapping(value = "/my-inquiriy/{id}")
    @Operation(summary = "내 문의 내역")
    public Map<UserInquiry, AdminAnswer> myInquiries(Authentication authentication, @PathVariable Long id) throws AccessDeniedException {
        return inquiryService.findMyInquiry(authentication, id);
    }
}
