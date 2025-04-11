package inxj.newsfeed.common.util;

import inxj.newsfeed.comment.entity.Comment;
import inxj.newsfeed.comment.repository.CommentRepository;
import inxj.newsfeed.exception.CustomException;
import inxj.newsfeed.exception.ErrorCode;
import inxj.newsfeed.friend.entity.FriendRequest;
import inxj.newsfeed.friend.repository.FriendRepository;
import inxj.newsfeed.like.entity.CommentLike;
import inxj.newsfeed.like.entity.CommentLikeId;
import inxj.newsfeed.like.entity.PostLike;
import inxj.newsfeed.like.entity.PostLikeId;
import inxj.newsfeed.like.repository.CommentLikeRepository;
import inxj.newsfeed.like.repository.PostLikeRepository;
import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.post.repository.PostRepository;
import inxj.newsfeed.user.entity.User;
import inxj.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static inxj.newsfeed.exception.ErrorCode.INVALID_FRIEND_REQUEST;

@Component
@RequiredArgsConstructor // 생성자가 한 개인 경우 자동으로 @Autowired 가 붙음
public class EntityFetcher {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final FriendRepository friendRepository;

    public User getUserOrThrow(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER_ID));
    }

    public Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST_ID));
    }

    public PostLike getPostLikeOrThrow(PostLikeId postLikeId) {
        return postLikeRepository.findById(postLikeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LIKE_ID));
    }

    public Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT_ID));
    }

    public CommentLike getCommentLikeOrThrow(CommentLikeId commentLikeId) {
        return commentLikeRepository.findById(commentLikeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LIKE_ID));
    }

    public FriendRequest getInteractiveFriendRequestOrThrow(User user1, User user2){
        return friendRepository.findInteractiveRequest(user1, user2)
                .orElseThrow(() -> new CustomException(INVALID_FRIEND_REQUEST));
    }

    public FriendRequest getFriendRequestOrThrow(User user1, User user2){
        return friendRepository.findByReceiverAndRequester(user1, user2)
                .orElseThrow(() -> new CustomException(INVALID_FRIEND_REQUEST));
    }

}