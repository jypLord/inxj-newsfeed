package inxj.newsfeed.domain.like.repository;

import inxj.newsfeed.domain.like.entity.CommentLike;
import inxj.newsfeed.domain.like.entity.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {
}
