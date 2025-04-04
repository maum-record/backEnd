package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.dto.UserRequest;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "로그인 회원 관련 기능")
public class UserController {
    private final UserService userService;

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
        return userService.findByEmail(authentication.getName());
    }

    @PostMapping(value = "/update")
    @Operation(summary = "회원 정보 수정")
    public ResponseEntity<String> updateUser(UserRequest request,Authentication authentication){
        userService.updateUser(request,authentication.getName());
        return ResponseEntity.ok("회원 정보 수정 완료");
    }

}
