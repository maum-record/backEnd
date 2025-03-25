package maumrecord.maumrecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import maumrecord.maumrecord.domain.Member;
import maumrecord.maumrecord.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Tag(name = "회원관리 서비스 API")
public class MemberController {
    private final MemberService memberService;
    public MemberController(MemberService memberService){this.memberService=memberService;}

    //테스트를 위한 home
    @GetMapping("/")
    @Operation(summary = "홈화면")
    public String home(){return "home";}
    /*
    * 회원 등록
    * */
    @GetMapping(value="/members/new")
    @Operation(summary = "회원 등록으로 이동")
    public String createForm(){return "members/createMemberForm";}

    @PostMapping(value="/members/new")
    @Operation(summary = "회원등록 정보 post")
    public String create(MemberForm form){
        Member member=new Member();
        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping(value = "/members")
    @Operation(summary = "회원목록 조회")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
