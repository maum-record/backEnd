package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.dto.UserRequest;
import maumrecord.maumrecord.dto.LoginRequest;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
@Tag(name = "비로그인 회원 관련 기능")
public class MainController {
    private final UserService userService;
    @PostMapping(value="/new")
    @Operation(summary = "회원가입")
    public ResponseEntity<String> signUp(@RequestBody UserRequest request){
        userService.signUp(request);
        return ResponseEntity.ok("회원가입 성공");
    }


    @PostMapping(value = "/login")
    @Operation(summary = "로그인")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){
        userService.login(request);
        return ResponseEntity.ok("로그인 성공");
    }
}
