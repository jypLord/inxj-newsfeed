package inxj.newsfeed.domain.like.service;

import inxj.newsfeed.domain.comment.entity.Comment;
import inxj.newsfeed.common.util.EntityFetcher;
import inxj.newsfeed.exception.customException.BaseException;
import inxj.newsfeed.exception.ErrorCode;
import inxj.newsfeed.domain.like.entity.CommentLike;
import inxj.newsfeed.domain.like.entity.CommentLikeId;
import inxj.newsfeed.domain.like.repository.CommentLikeRepository;
import inxj.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 생성자가 한 개인 경우 자동으로 @Autowired 가 붙음
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final EntityFetcher entityFetcher;

    public void like(Long commentId, Long userId) {
        // 복합키 생성
        Comment comment = entityFetcher.getCommentOrThrow(commentId);
        User user = entityFetcher.getUserOrThrow(userId);
        CommentLikeId commentLikeId = new CommentLikeId(commentId, userId);

        // 중복 체크
        if (commentLikeRepository.findById(commentLikeId).isPresent()) {
            throw new BaseException(ErrorCode.CONFLICT_STATUS);
        }

        // 저장
        commentLikeRepository.save(new CommentLike(commentLikeId, comment, user));
    }

    public void unlike(Long commentId, Long userId) {
        // 복합키로 데이터 검색
        CommentLikeId commentLikeId = new CommentLikeId(commentId, userId);
        CommentLike found = entityFetcher.getCommentLikeOrThrow(commentLikeId);

        // 삭제
        commentLikeRepository.delete(found);
    }

}
