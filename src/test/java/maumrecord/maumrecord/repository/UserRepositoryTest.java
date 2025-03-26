package maumrecord.maumrecord.repository;

import jakarta.transaction.Transactional;
import maumrecord.maumrecord.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository repository;

    @Test
    void save() {
        //given
        User m = new User();
        m.setName("test");
        //when
        repository.save(m);
        //then
        User result = repository.findById(m.getId()).get();
        assertThat(result).isEqualTo(m);
    }

    @Test
    void findByName() {
        //given
        User m1=new User();
        m1.setName("test1");
        repository.save(m1);

        User m2=new User();
        m2.setName("test2");
        repository.save(m2);

        //when
        User result=repository.findByName("test1").get();

        //then
        assertThat(result).isEqualTo(m1);
    }

    @Test
    void findAll() {
        //given
        User m1=new User();
        m1.setName("test1");
        repository.save(m1);

        User m2=new User();
        m2.setName("test2");
        repository.save(m2);

        //when
        List<User> result=repository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }
}