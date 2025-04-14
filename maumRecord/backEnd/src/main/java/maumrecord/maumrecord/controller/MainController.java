package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.dto.CreateAccessTokenRequest;
import maumrecord.maumrecord.dto.CreateAccessTokenResponse;
import maumrecord.maumrecord.dto.LoginRequest;
import maumrecord.maumrecord.dto.UserRequest;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "비로그인 회원 관련 기능")
public class MainController {
    private final UserService userService;

    @PostMapping(value="/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<String> signUp(@RequestBody UserRequest request){
        userService.signUp(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping(value = "/login")
    @Operation(summary = "로그인")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request){
        String accessToken=userService.login(request);
        String refreshToken = userService.findRefreshToken(request.getEmail());
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        //엑세스 토큰과 리프레시 토큰 전달
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/newToken")
    public ResponseEntity<CreateAccessTokenResponse> createAccessToken(@RequestBody CreateAccessTokenRequest request){
        String newAccessToken=userService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }

    //todo: 테스트용
    @GetMapping(value = "/users")
    @Operation(summary = "회원목록 조회- 테스트용")
    public List<User> list() {
        return userService.findUsers();
    }

}
