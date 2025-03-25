package maumrecord.maumrecord.service;

import jakarta.transaction.Transactional;
import maumrecord.maumrecord.domain.Member;
import maumrecord.maumrecord.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository repository;
    @Autowired
    MemberService memberService;

    @Test
    void join() throws Exception {
        //given
        Member member=new Member();
        member.setName("test");

        //when
        Long saveId=memberService.join(member);

        //then
        Member findMember=repository.findById(saveId).get();
        assertEquals(member.getName(),findMember.getName());
    }

    @Test
    void duplicatedMember() throws Exception{
        //given
        Member member1=new Member();
        member1.setName("test");
        Member member2=new Member();
        member2.setName("test");

        //when
        memberService.join(member1);
        IllegalStateException e=assertThrows(IllegalStateException.class,()->memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}