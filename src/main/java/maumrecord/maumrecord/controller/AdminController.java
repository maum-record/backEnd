package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.service.UserDetailService;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "관리자 관련")
public class AdminController {
    private UserService userService;
    public AdminController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    @Operation(summary = "회원목록 조회")
    public List<User> list() {
        return userService.findMembers();
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
    //todo: add 관리자 대시보드, 관리자 로그인 처리(별도 테이블 vs users에서 컬럼으로 구별),
}
