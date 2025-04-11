package inxj.newsfeed.domain.comment.service;

import inxj.newsfeed.domain.comment.entity.Comment;
import inxj.newsfeed.domain.comment.repository.CommentRepository;
import inxj.newsfeed.domain.comment.dto.RequestDto;
import inxj.newsfeed.domain.comment.dto.ResponseDto;
import inxj.newsfeed.common.util.EntityFetcher;
import inxj.newsfeed.exception.CustomException;
import inxj.newsfeed.domain.post.entity.Post;
import inxj.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static inxj.newsfeed.exception.ErrorCode.UNAUTHORIZED_USER_ID;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final EntityFetcher entityFetcher;

    //생성
    public void saveComment(Long id, Long postId, RequestDto requestDto) {
        User user = entityFetcher.getUserOrThrow(id);

        Post post = entityFetcher.getPostOrThrow(postId);

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

        Comment comment = entityFetcher.getCommentOrThrow(commentId);

        if (!userId.equals(comment.getUser().getId())&&!userId.equals(comment.getPost().getUser().getId())) {
            throw new CustomException(UNAUTHORIZED_USER_ID);
        }

        comment.updateContent(requestDto.getComment());
    }

    //삭제
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = entityFetcher.getCommentOrThrow(commentId);
        if (!userId.equals(comment.getUser().getId())&&!userId.equals(comment.getPost().getUser().getId())) {
            throw new CustomException(UNAUTHORIZED_USER_ID);
        }
        commentRepository.deleteById(commentId);
    }

}
