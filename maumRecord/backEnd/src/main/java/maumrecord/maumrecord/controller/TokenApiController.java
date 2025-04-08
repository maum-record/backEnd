package maumrecord.maumrecord.controller;

import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.dto.CreateAccessTokenRequest;
import maumrecord.maumrecord.dto.CreateAccessTokenResponse;
import maumrecord.maumrecord.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    //todo: 토큰 이해 후 현재 페이지 수정
    @PostMapping("/newToken")
    public ResponseEntity<CreateAccessTokenResponse> createAccessToken(@RequestBody CreateAccessTokenRequest request){
        String newAccessToken=tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }
}
