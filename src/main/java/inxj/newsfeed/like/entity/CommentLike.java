package inxj.newsfeed.like.entity;

import inxj.newsfeed.comment.entity.Comment;
import inxj.newsfeed.common.entity.BaseEntity;
import inxj.newsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike extends BaseEntity {

    @EmbeddedId // 복합키
    private CommentLikeId id;

    // 댓글:댓글 좋아요  = 1:N 단방향 관계 (한 댓글에 좋아요가 여러 개)
    @ManyToOne
    @MapsId("commentId") // 복합키의 필드명과 연결
    @JoinColumn(name = "comment_id", nullable = false) // DB의 외래키 컬럼명
    private Comment comment;

    // 유저:댓글 좋아요 = 1:N 단방향 관계 (한 유저가 여러 댓글에 좋아요)
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
