package inxj.newsfeed.like.entity;

import inxj.newsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class PostLike extends BaseEntity {

    @EmbeddedId
    private PostLikeId id;

    // 게시글:게시글 좋아요  = 1:N 단방향 관계 (한 게시글에 좋아요가 여러 개)
    @ManyToOne
    @MapsId("postId") // 복합키의 필드명과 연결
    @JoinColumn(name = "post_id") // DB의 외래키 컬럼명
    private Post post;

    // 유저:게시글 좋아요 = 1:N 단방향 관계 (한 유저가 여러 게시글에 좋아요)
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

}
