package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.dto.UserRequest;
import maumrecord.maumrecord.dto.LoginRequest;
import maumrecord.maumrecord.service.TokenService;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "비로그인 회원 관련 기능")
public class MainController {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping(value="/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<String> signUp(@RequestBody UserRequest request){
        userService.signUp(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping(value = "/login")
    @Operation(summary = "로그인")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){
        userService.login(request);
        String refreshToken= userService.findRefreshToken(request.getEmail());
        tokenService.createNewAccessToken(refreshToken);
        userService.setRefreshToken(refreshToken, request.getEmail());

        return ResponseEntity.ok("로그인 성공");
    }
}
