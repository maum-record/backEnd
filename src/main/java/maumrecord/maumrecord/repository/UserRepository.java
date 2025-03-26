package maumrecord.maumrecord.repository;

import jakarta.persistence.EntityManager;
import maumrecord.maumrecord.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//ToDo:login 로그인을 위한 email-password 추가
@Repository
public class UserRepository {
    private final EntityManager em;
    public UserRepository(EntityManager em){
        this.em=em;
    }

    public User save(User user){
        em.persist(user);
        return user;
    }

    public Optional<User> findById(Long id){
        User user =em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByName(String name){
        List<User> result=em.createQuery("select m from User m where m.name=:name", User.class)
                .setParameter("name",name)
                .getResultList();
        return result.stream().findAny();
    }

    public List<User> findAll(){
        return em.createQuery("select m from User m", User.class).getResultList();
    }

    //테스트용
    public void clearStore() {
        em.clear();
    }
}
