package maumrecord.maumrecord.repository;

import jakarta.transaction.Transactional;
import maumrecord.maumrecord.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository repository;

    @Test
    void save() {
        //given
        Member m = new Member();
        m.setName("test");
        //when
        repository.save(m);
        //then
        Member result = repository.findById(m.getId()).get();
        assertThat(result).isEqualTo(m);
    }

    @Test
    void findByName() {
        //given
        Member m1=new Member();
        m1.setName("test1");
        repository.save(m1);

        Member m2=new Member();
        m2.setName("test2");
        repository.save(m2);

        //when
        Member result=repository.findByName("test1").get();

        //then
        assertThat(result).isEqualTo(m1);
    }

    @Test
    void findAll() {
        //given
        Member m1=new Member();
        m1.setName("test1");
        repository.save(m1);

        Member m2=new Member();
        m2.setName("test2");
        repository.save(m2);

        //when
        List<Member> result=repository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }
}