package inxj.newsfeed.like.service;

import inxj.newsfeed.common.enums.Message;
import inxj.newsfeed.like.entity.PostLike;
import inxj.newsfeed.like.entity.PostLikeId;
import inxj.newsfeed.like.repository.PostLikeRepository;
import inxj.newsfeed.post.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PostLikeService {

    private PostLikeRepository postLikeRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostLikeService(PostLikeRepository postLikeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void like(Long postId, Long userId) {
        Post post = postRepository.findById(postId);
        User user = userRepository.findById(userId);
        PostLikeId id = new PostLikeId(postId, userId);
        postLikeRepository.save(new PostLike(id, post, user));
    }

    public void unlike(Long postId, Long userId) {
        PostLikeId id = new PostLikeId(postId, userId);
        PostLike found = postLikeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Message.NO_ELEMENT.get()));
        postLikeRepository.delete(found);
    }

}
