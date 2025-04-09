package inxj.newsfeed.like.repository;

import inxj.newsfeed.like.entity.CommentLike;
import inxj.newsfeed.like.entity.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {
}
