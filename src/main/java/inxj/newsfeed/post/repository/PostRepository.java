package inxj.newsfeed.post.repository;

import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.post.entity.Visibility;
import inxj.newsfeed.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 모든 전체 공개 게시글을 생성일 순으로 조회
    List<Post> findAllByVisibilityOrderByCreatedAtDesc(Visibility visibility);

    // 모든 친구 공개 게시글을 생성일 순으로 조회
    List<Post> findAllByVisibilityAndUserInOrderByCreatedAtDesc(Visibility visibility, List<User> friends);

    // 사용자의 모든 전체 공개 게시글을 생성일 순으로 조회
    List<Post> findAllByUserAndVisibilityOrderByCreatedAtDesc(User user, Visibility visibility);

    // 유저의 모든 게시글을 생성일 순으로 조회
    List<Post> findAllByUserOrderByCreatedAtDesc(User user);
}
