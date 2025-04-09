package inxj.newsfeed.like.service;

import inxj.newsfeed.like.entity.PostLike;
import inxj.newsfeed.like.entity.PostLikeId;
import inxj.newsfeed.like.repository.PostLikeRepository;
import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.user.User;
import inxj.newsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 생성자가 한 개인 경우 자동으로 @Autowired 가 붙음
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final EntityFetcher entityFetcher;

    public void like(Long postId, Long userId) {
        Post post = entityFetcher.getPostOrThrow(postId);
        User user = entityFetcher.getUserOrThrow(userId);
        PostLikeId postLikeId = new PostLikeId(postId, userId);
        postLikeRepository.save(new PostLike(postLikeId, post, user));
    }

    public void unlike(Long postId, Long userId) {
        PostLikeId postLikeId = new PostLikeId(postId, userId);
        PostLike found = entityFetcher.getPostLikeOrThrow(postLikeId);
        postLikeRepository.delete(found);
    }

}
