package inxj.newsfeed.like.repository;

import inxj.newsfeed.like.entity.PostLike;
import inxj.newsfeed.like.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
}
