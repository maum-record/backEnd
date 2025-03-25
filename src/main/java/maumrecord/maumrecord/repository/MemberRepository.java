package maumrecord.maumrecord.repository;

import jakarta.persistence.EntityManager;
import maumrecord.maumrecord.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//ToDo:login 로그인을 위한 email-password 추가
@Repository
public class MemberRepository {
    private final EntityManager em;
    public MemberRepository(EntityManager em){
        this.em=em;
    }

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Optional<Member> findById(Long id){
        Member member=em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByName(String name){
        List<Member> result=em.createQuery("select m from Member m where m.name=:name",Member.class)
                .setParameter("name",name)
                .getResultList();
        return result.stream().findAny();
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    //테스트용
    public void clearStore() {
        em.clear();
    }
}
