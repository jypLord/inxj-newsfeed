package inxj.newsfeed.comment.service;

import inxj.newsfeed.comment.entity.Comment;
import inxj.newsfeed.comment.repository.CommentRepository;
import inxj.newsfeed.comment.dto.RequestDto;
import inxj.newsfeed.comment.dto.ResponseDto;
import inxj.newsfeed.exception.CustomException;
import inxj.newsfeed.exception.ErrorCode;
import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.post.repository.PostRepository;
import inxj.newsfeed.user.entity.User;
import inxj.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //생성
    public void saveComment(Long id, Long postId, RequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST_ID));

        Comment comment = new Comment(user, post, requestDto.getComment());

        commentRepository.save(comment);
    }

    //전체조회 paging 할까요?
    @Transactional(readOnly = true)
    public List<ResponseDto> findComment(Long postId) {

        return commentRepository.findByPostId(postId).stream()
                .map(commentEntity -> new ResponseDto(commentEntity.getId(),
                        commentEntity.getContent(),
                        commentEntity.getCreatedAt(),
                        commentEntity.getUpdatedAt()))
                .toList();
    }

    //수정
    @Transactional
    public void updateComment(Long userId, Long commentId, RequestDto requestDto) {

        Comment comment = commentRepository.findWithUserAndPostUserById(commentId).
                orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT_ID));

        if (!userId.equals(comment.getUser().getId())&&!userId.equals(comment.getPost().getUser().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER_ID);
        }

        comment.updateContent(requestDto.getComment());
    }

    //삭제
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findWithUserAndPostUserById(commentId).
                orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT_ID));
        if (!userId.equals(comment.getUser().getId())&&!userId.equals(comment.getPost().getUser().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER_ID);
        }
        commentRepository.deleteById(commentId);
    }

}
