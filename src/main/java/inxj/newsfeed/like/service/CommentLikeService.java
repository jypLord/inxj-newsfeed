package inxj.newsfeed.like.service;

import inxj.newsfeed.common.enums.Message;
import inxj.newsfeed.like.entity.CommentLike;
import inxj.newsfeed.like.entity.CommentLikeId;
import inxj.newsfeed.like.repository.CommentLikeRepository;
import inxj.newsfeed.user.User;
import inxj.newsfeed.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor // 생성자가 한 개인 경우 자동으로 @Autowired 가 붙음
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EntityFetcher entityFetcher;

    public void like(Long commentId, Long userId) {
        Comment comment = entityFetcher.getCommentOrThrow(commentId);
        User user = entityFetcher.getUserOrThrow(userId);
        CommentLikeId commentLikeId = new CommentLikeId(commentId, userId);
        commentLikeRepository.save(new CommentLike(commentLikeId, comment, user));
    }

    public void unlike(Long commentId, Long userId) {
        CommentLikeId commentLikeId = new CommentLikeId(commentId, userId);
        CommentLike found = entityFetcher.getCommentLikeOrThrow(commentLikeId);
        commentLikeRepository.delete(found);
    }

}
