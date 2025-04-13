package inxj.newsfeed.domain.like.service;

import inxj.newsfeed.common.util.EntityFetcher;
import inxj.newsfeed.exception.BaseException;
import inxj.newsfeed.exception.ErrorCode;
import inxj.newsfeed.domain.like.entity.PostLike;
import inxj.newsfeed.domain.like.entity.PostLikeId;
import inxj.newsfeed.domain.like.repository.PostLikeRepository;
import inxj.newsfeed.domain.post.entity.Post;
import inxj.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 생성자가 한 개인 경우 자동으로 @Autowired 가 붙음
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final EntityFetcher entityFetcher;

    public void like(Long postId, Long userId) {
        // 복합키 생성
        Post post = entityFetcher.getPostOrThrow(postId);
        User user = entityFetcher.getUserOrThrow(userId);
        PostLikeId postLikeId = new PostLikeId(postId, userId);

        // 중복 체크
        if (postLikeRepository.findById(postLikeId).isPresent()) {
            throw new BaseException(ErrorCode.CONFLICT_STATUS);
        }

        // 저장
        postLikeRepository.save(new PostLike(postLikeId, post, user));
    }

    public void unlike(Long postId, Long userId) {
        // 복합키로 데이터 검색
        PostLikeId postLikeId = new PostLikeId(postId, userId);
        PostLike found = entityFetcher.getPostLikeOrThrow(postLikeId);

        // 삭제
        postLikeRepository.delete(found);
    }

}