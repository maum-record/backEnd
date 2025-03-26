package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.dto.AddUserRequest;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "회원 관련")
public class UserController {
    private final UserService userService;

    @PostMapping(value="/new")
    @Operation(summary = "회원가입")
    public ResponseEntity<String> signUp(@RequestBody AddUserRequest request){
        userService.save(request);
        return ResponseEntity.ok("회원가입 성공");
    }
    //todo: 로그아웃, 회원정보 수정, 비밀번호 저장방식?, 로그인 방식 업데이트

}
