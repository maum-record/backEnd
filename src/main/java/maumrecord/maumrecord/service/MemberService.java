package maumrecord.maumrecord.service;

import jakarta.transaction.Transactional;
import maumrecord.maumrecord.domain.Member;
import maumrecord.maumrecord.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    public final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository){this.memberRepository=memberRepository;}

    /*
    * 회원 가입
    * */
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        memberRepository.findByName(member.getName())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /*
    * 전체 회원 조회
    * */
    public List<Member> findMembers(){return memberRepository.findAll();}
    public Optional<Member> findOne(Long memberId){return memberRepository.findById(memberId);}
}
