package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import maumrecord.maumrecord.domain.Member;
import maumrecord.maumrecord.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "관리자 관련")
public class AdminController {
    private MemberService memberService;
    public AdminController(MemberService memberService){this.memberService=memberService;}

    @GetMapping(value = "/list")
    @Operation(summary = "회원목록 조회")
    public List<Member> list() {
        return memberService.findMembers();
    }
}
