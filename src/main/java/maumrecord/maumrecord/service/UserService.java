package maumrecord.maumrecord.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.domain.User;
import maumrecord.maumrecord.dto.AddUserRequest;
import maumrecord.maumrecord.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto){
        validateDuplicateMember(dto);
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    //todo: 기본 제공 기능인지 확인
    private void validateDuplicateMember(AddUserRequest dto){
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<User> findMembers(){return userRepository.findAll();}
    public Optional<User> findOne(Long memberId){return userRepository.findById(memberId);}
}
