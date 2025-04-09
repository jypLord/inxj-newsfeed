package inxj.newsfeed.post.repository;

import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.post.entity.Visibility;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface PostRepository extends JpaRepository<Post, Long> {

  // 모든 전체 공개 게시글을 생성일 순으로 조회
  List<Post> findAllByVisibilityOrderByCreatedAtDesc(Visibility visibility);
}
