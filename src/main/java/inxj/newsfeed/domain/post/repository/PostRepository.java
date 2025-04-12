package inxj.newsfeed.domain.post.repository;

import inxj.newsfeed.domain.post.entity.Category;
import inxj.newsfeed.domain.post.entity.Post;
import inxj.newsfeed.domain.post.entity.Visibility;
import inxj.newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
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
    @Query("SELECT p FROM Post p JOIN p.categoryIds c WHERE p.visibility = :visibility AND c IN :categoryList ORDER BY p.createdAt DESC")
    List<Post> findAllByCategoryAndVisibility(
        @Param("visibility") Visibility visibility, @Param("categoryList")List<Category> categoryList);

}
