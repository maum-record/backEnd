package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.dto.LoginRequest;
import maumrecord.maumrecord.dto.UserRequest;
import maumrecord.maumrecord.service.TokenService;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        userService.login(request);
        String refreshToken= userService.findRefreshToken(request.getEmail());
        tokenService.createNewAccessToken(refreshToken);
        userService.setRefreshToken(refreshToken, request.getEmail());

        return ResponseEntity.ok("로그인 성공");
    }
    //todo: 테스트용
    @GetMapping(value = "/users")
    @Operation(summary = "회원목록 조회- 테스트용")
    public List<User> list() {
        return userService.findUsers();
    }

}
