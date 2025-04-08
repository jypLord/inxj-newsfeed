package inxj.newsfeed.like.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

// 복합키 클래스
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeId implements Serializable {

    @Column(name = "post_id", nullable = false)
    private Long postId; // 좋아요한 게시글

    @Column(name = "user_id", nullable = false)
    private Long userId; // 좋아요를 누른 유저

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // 메모리 주소로 비교 (동일성)
            return true;
        }

        if (!(obj instanceof PostLikeId)) {
            return false;
        }

        PostLikeId that = (PostLikeId) obj;
        return Objects.equals(postId, that.postId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }

}
