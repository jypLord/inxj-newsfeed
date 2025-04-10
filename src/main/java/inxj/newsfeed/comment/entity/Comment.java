package inxj.newsfeed.comment.entity;

import inxj.newsfeed.common.entity.BaseEntity;
import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Post post;

    @Column(columnDefinition = "longtext")
    String content;

    public Comment(User user, Post post, String content){
        this.post=post;
        this.user=user;
        this.content=content;
    }

    //세터 사용 안하는 이유 : null체크랑 통일성 키울려고 이렇게 합니다
    public void updateContent(String content) {
       if (content.isBlank()){
           throw new RuntimeException("custom으로 바꿔요");
       }
       this.content = content;
    }
}
