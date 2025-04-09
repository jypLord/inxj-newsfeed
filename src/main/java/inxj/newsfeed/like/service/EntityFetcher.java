package inxj.newsfeed.like.service;

import inxj.newsfeed.common.enums.Message;
import inxj.newsfeed.like.entity.CommentLike;
import inxj.newsfeed.like.entity.CommentLikeId;
import inxj.newsfeed.like.entity.PostLike;
import inxj.newsfeed.like.entity.PostLikeId;
import inxj.newsfeed.like.repository.CommentLikeRepository;
import inxj.newsfeed.like.repository.PostLikeRepository;
import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.user.User;
import inxj.newsfeed.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor // 생성자가 한 개인 경우 자동으로 @Autowired 가 붙음
public class EntityFetcher {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(Message.NO_ELEMENT.get()));
    }

    public Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException(Message.NO_ELEMENT.get()));
    }

    public PostLike getPostLikeOrThrow(PostLikeId postLikeId) {
        return postLikeRepository.findById(postLikeId)
                .orElseThrow(() -> new NoSuchElementException(Message.NO_ELEMENT.get()));
    }

    public Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException(Message.NO_ELEMENT.get()));
    }

    public CommentLike getCommentLikeOrThrow(CommentLikeId commentLikeId) {
        return commentLikeRepository.findById(commentLikeId)
                .orElseThrow(() -> new NoSuchElementException(Message.NO_ELEMENT.get()));
    }

}