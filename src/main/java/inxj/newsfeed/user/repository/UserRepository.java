package inxj.newsfeed.user.repository;

import inxj.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    // 검색한 문자가 포함돼있는 유저의 닉네임과 프로필 사진 가져오기
    @Query(value = "SELECT username, profileImageUrl FROM users WHERE username LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<User> findByUsername(String username);
}
