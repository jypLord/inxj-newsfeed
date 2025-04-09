package inxj.newsfeed.user.repository;

import inxj.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<user> findByPhoneNumber(String phoneNumber);
}
