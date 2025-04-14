package inxj.newsfeed.domain.like.repository;

import inxj.newsfeed.domain.like.entity.PostLike;
import inxj.newsfeed.domain.like.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
}
