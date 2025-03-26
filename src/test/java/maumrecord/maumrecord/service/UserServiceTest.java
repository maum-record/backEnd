package maumrecord.maumrecord.service;

import jakarta.transaction.Transactional;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository repository;
    @Autowired
    UserService userService;

    @Test
    void join() throws Exception {
        //given
        User user =new User();
        user.setName("test");

        //when
        Long saveId= userService.join(user);

        //then
        User findUser =repository.findById(saveId).get();
        assertEquals(user.getName(), findUser.getName());
    }

    @Test
    void duplicatedMember() throws Exception{
        //given
        User user1 =new User();
        user1.setName("test");
        User user2 =new User();
        user2.setName("test");

        //when
        userService.join(user1);
        IllegalStateException e=assertThrows(IllegalStateException.class,()-> userService.join(user2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}