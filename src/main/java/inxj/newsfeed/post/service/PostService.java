package inxj.newsfeed.post.service;

import static inxj.newsfeed.exception.ErrorCode.INVALID_USER_ID;
import static inxj.newsfeed.friend.entity.Status.ACCEPT;

import inxj.newsfeed.exception.CustomException;
import inxj.newsfeed.friend.repository.FriendRepository;
import inxj.newsfeed.post.dto.PostCreateRequestDTO;
import inxj.newsfeed.post.dto.PostResponseDTO;
import inxj.newsfeed.post.dto.PostUpdateRequestDTO;
import inxj.newsfeed.post.entity.Category;
import inxj.newsfeed.post.entity.CategoryType;
import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.post.entity.Visibility;
import inxj.newsfeed.post.repository.CategoryRepository;
import inxj.newsfeed.post.repository.PostRepository;
import inxj.newsfeed.user.User;
import inxj.newsfeed.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;
  private final FriendRepository friendRepository;

  // TODO: CustomException 반영

  // 게시글 작성
  public void save(PostCreateRequestDTO requestDTO, Long userId) {
    User user = userRepository.findById(userId).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저 id = "+userId)); // 사용자 찾기
    postRepository.save(new Post(requestDTO, user));
  }

  // 게시글 단건 조회
  public PostResponseDTO find(Long postId, Long userId) {
    Post post = postRepository.findById(postId).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 포스트 id = "+postId));

    // 다른 사람의 친구 공개 게시글이라면
    if(post.getVisibility() == Visibility.FRIENDS && !(post.getUser().getId().equals(userId))) {
      // TODO: post의 작성자와 친구 여부 확인
    }
    return new PostResponseDTO(post);
  }

  // 모든 전체 공개 게시글 조회
  public List<PostResponseDTO> findAllPublicPosts() {
    // 전체 공개 게시글에 대하여 작성일자 내림차순 조회
    List<Post> postList = postRepository.findAllByVisibilityOrderByCreatedAtDesc(Visibility.PUBLIC);
    return postList.stream()
        .map(PostResponseDTO::new).toList();
  }

  // 모든 친구 공개 게시글 조회
  public List<PostResponseDTO> findAllFriendPosts(Long loginId) {
    User user = userRepository.findById(loginId).orElseThrow(() -> new CustomException(INVALID_USER_ID));

    // 친구 목록 조회
    List<User> friendList = friendRepository.findByUserAndStatus(user, ACCEPT);

    // 친구인 사용자들의 모든 친구 공개 게시글 조회
    List<Post> postList = postRepository.findAllByVisibilityAndUserInOrderByCreatedAtDesc(Visibility.FRIENDS, friendList);
    return postList.stream()
        .map(PostResponseDTO::new).toList();
  }

  // 사용자의 모든 게시글 조회
  public List<PostResponseDTO> findAllUserPosts(Long targetUserId, Long loginId) {
    List<Post> postList;
    User targetUser = userRepository.findById(targetUserId)
        .orElseThrow(() -> new CustomException(INVALID_USER_ID));

    // 자신의 모든 게시글을 조회
    if(targetUserId.equals(loginId)) {
      postList = postRepository.findAllByUserOrderByCreatedAtDesc(targetUser);
    }
    // 다른 유저의 모든 게시글을 조회
    else {
      User user = userRepository.findById(loginId).orElseThrow(() -> new CustomException(INVALID_USER_ID));
      List<User> friendList = friendRepository.findByUserAndStatus(user, ACCEPT);

      // 친구라면 전체 공개와 친구 공개 게시글 조회
      if(friendList.contains(user)) {
        postList = postRepository.findAllByUserOrderByCreatedAtDesc(targetUser);
      }
      // 전체 공개 게시글만 조회
      else {
        postList = postRepository.findAllByUserAndVisibilityOrderByCreatedAtDesc(targetUser, Visibility.PUBLIC);
      }
    }

    return postList.stream()
        .map(PostResponseDTO::new).toList();
  }

  // 모든 전체 공개 게시글을 카테고리 별로 조회
  public List<PostResponseDTO> findAllPublicPostsByCategories(List<CategoryType> categoryTypeList) {
    // enum 검증은 컨트롤러 레이어에서

    // List <CategoryType> --> List <Category>
    // 중복값은 JPA IN 쿼리에서 자동으로 무시
    List<Category> categoryList = categoryRepository.findByCategoryTypeIn(categoryTypeList);

    // 모든 전체 공개 게시글을 카테고리 별로 조회
    List<Post> postList = postRepository.findAllByVisibilityAndCategoryIdsIn(Visibility.PUBLIC, categoryList);

    return postList.stream()
        .map(PostResponseDTO::new).toList();
  }

  // 게시글 수정
  @Transactional
  public void updatePost(Long postId, PostUpdateRequestDTO requestDTO, Long loginId) {
    Post targetPost = postRepository.findById(postId).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 포스트 id = " + postId));  // 게시글 찾기
    User targetUser = targetPost.getUser();

    // 수정 대상 게시글 작성자가 현재 로그인한 사용자와 동일하다면
    if(loginId.equals(targetUser.getId())) {
      targetPost.update(requestDTO);    // 업데이트
    }
    // 일치하지 않는 경우
    else {
      throw new RuntimeException("게시글 ID = "+postId+" 의 작성자가 아닙니다.");
    }
  }
}
