package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.dto.LoginRequest;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "로그인 회원 관련 기능")
public class UserController {
    private final UserService userService;

    //todo: 로그아웃, 회원정보 수정, 비밀번호 저장방식?, 로그인 방식 업데이트
    @GetMapping(value="/delete")
    @Operation(summary = "회원탈퇴")
    public ResponseEntity<String> deleteUser(Authentication authentication)
    {
        String token=(String)authentication.getCredentials();
        userService.deleteUser(authentication);
        return ResponseEntity.ok("회원탈퇴 완료");
    }

}
