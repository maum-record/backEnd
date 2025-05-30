package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
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
        return userService.findUsers();
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
    //todo: 관리자 답변 부분 정해지면 작성
    //@PostMapping(value = "")
}
