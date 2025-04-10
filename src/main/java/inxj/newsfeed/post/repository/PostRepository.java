package inxj.newsfeed.post.repository;

import inxj.newsfeed.post.entity.Category;
import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.post.entity.Visibility;
import inxj.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
  // TODO: 메소드명 다시 짓거나 줄이기

    // 모든 게시글을 게시글 공개 범위에 따라 생성일 순으로 조회
    @Query("SELECT p FROM Post p WHERE p.visibility = :visibility ORDER BY p.createdAt DESC")
    List<Post> findAllByVisibility(@Param("visibility") Visibility visibility);

    // 모든 친구 공개 게시글을 서로 친구라면 생성일 순으로 조회
    @Query("SELECT p FROM Post p WHERE p.visibility = :visibility AND p.user IN :users ORDER BY p.createdAt DESC")
    List<Post> findAllFriendVisible(
        @Param("visibility") Visibility visibility, @Param("users") List<User> friends);

    // 사용자의 모든 전체 공개 게시글을 생성일 순으로 조회
    @Query("SELECT p FROM Post p WHERE p.user = :user AND p.visibility = :visibility ORDER BY p.createdAt DESC")
    List<Post> findAllByUserAndVisibility(@Param("user") User user, @Param("visibility") Visibility visibility);

    // 유저의 모든 게시글을 생성일 순으로 조회
    @Query("SELECT p FROM Post p WHERE p.user = :user ORDER BY p.createdAt DESC")
    List<Post> findAllByUser(@Param("user") User user);

    // 공개 범위와 카테고리 별로 조회
    @Query("SELECT p FROM Post p WHERE p.visibility = :visibility AND p.categoryIds IN :categories ORDER BY p.createdAt DESC")
    List<Post> findAllByCategoryAndVisibility(
        @Param("visibility") Visibility visibility, @Param("categories")List<Category> categoryList);

}
