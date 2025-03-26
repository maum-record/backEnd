package maumrecord.maumrecord.service;

import jakarta.transaction.Transactional;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    public final UserRepository userRepository;
    public UserService(UserRepository userRepository){this.userRepository = userRepository;}

    /*
    * 회원 가입
    * */
    public Long join(User user){
        validateDuplicateMember(user);
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(User user){
        userRepository.findByName(user.getName())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /*
    * 전체 회원 조회
    * */
    public List<User> findMembers(){return userRepository.findAll();}
    public Optional<User> findOne(Long memberId){return userRepository.findById(memberId);}
}
