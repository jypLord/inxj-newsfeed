package inxj.newsfeed.like.service;

import inxj.newsfeed.comment.Comment;
import inxj.newsfeed.comment.CommentRepository;
import inxj.newsfeed.common.enums.Message;
import inxj.newsfeed.like.entity.CommentLike;
import inxj.newsfeed.like.entity.CommentLikeId;
import inxj.newsfeed.like.repository.CommentLikeRepository;
import inxj.newsfeed.user.User;
import inxj.newsfeed.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentLikeService(CommentLikeRepository commentLikeRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }
    public void like(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId);
        User user = userRepository.findById(userId);
        CommentLikeId id = new CommentLikeId(commentId, userId);
        commentLikeRepository.save(new CommentLike(id, comment, user));
    }

    public void unlike(Long commentId, Long userId) {
        CommentLikeId id = new CommentLikeId(commentId, userId);
        CommentLike found = commentLikeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Message.NO_ELEMENT.get()));
        commentLikeRepository.delete(found);
    }

}
