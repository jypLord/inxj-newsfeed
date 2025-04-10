package inxj.newsfeed.comment.repository;

import inxj.newsfeed.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.post.id = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.post p JOIN FETCH p.user WHERE c.id = :commentId")
    Optional<Comment> findWithUserAndPostUserById(@Param("commentId") Long commentId);

}