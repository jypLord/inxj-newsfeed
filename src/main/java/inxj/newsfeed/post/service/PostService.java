package inxj.newsfeed.post.service;

import inxj.newsfeed.post.dto.PostCreateRequestDTO;
import inxj.newsfeed.post.dto.PostResponseDTO;
import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.post.entity.Visibility;
import inxj.newsfeed.post.repository.PostRepository;
import inxj.newsfeed.user.User;
import inxj.newsfeed.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final FriendRepository friendRepository;

  // 게시글 작성
  public void save(PostCreateRequestDTO requestDTO, Long userId) {
    User user = userRepository.findById(userId).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저 id = "+userId)); // 사용자 찾기
    postRepository.save(new Post(requestDTO, user));
  }

  // 게시글 단건 조회
  public PostResponseDTO find(Long id, Long userId) {
    Post post = postRepository.findById(id).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 포스트 id = "+id));

    // 다른 사람의 친구 공개 게시글이라면
    if(post.getVisibility() == Visibility.FRIENDS && post.getUser().getId() != userId) {
      // TODO: post의 작성자와 친구 여부 확인
    }
    return new PostResponseDTO(post);
  }

  // 모든 전체 공개 게시글 조회
  public List<PostResponseDTO> findAllPublicPosts(Long userId) {
    // 전체 공개 게시글에 대하여 작성일자 내림차순 조회
    List<Post> postList = postRepository.findAllByVisibilityOrderByCreatedAtDesc(Visibility.PUBLIC);
    return postList.stream()
        .map(PostResponseDTO::new).toList();
  }
}
