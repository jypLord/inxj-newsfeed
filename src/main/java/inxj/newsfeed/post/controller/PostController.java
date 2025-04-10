package inxj.newsfeed.post.controller;

import inxj.newsfeed.post.dto.PostCreateRequestDto;
import inxj.newsfeed.post.dto.PostResponseDto;
import inxj.newsfeed.post.dto.PostUpdateRequestDto;
import inxj.newsfeed.post.entity.CategoryType;
import inxj.newsfeed.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostController {
  private final PostService postService;

  // 게시글 생성
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody PostCreateRequestDto requestDTO, HttpSession session) {
    Long userId = (Long)session.getAttribute("loginUser");
    postService.save(requestDTO, userId);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  // 게시글 단건 조회
  @GetMapping("/{postId}")
  public ResponseEntity<PostResponseDto> find(@PathVariable Long postId, HttpSession session) {
    Long userId = (Long)session.getAttribute("loginUser");
    return new ResponseEntity<>(postService.find(postId, userId), HttpStatus.OK);
  }

  // 모든 전체 공개 게시글 조회
  // 조건: 전체, 카테고리
  // TODO: Category List 검증 로직 필요
  @GetMapping
  public ResponseEntity<List<PostResponseDto>> findAllPublicPosts(
      @RequestParam(required = false) List<CategoryType> categoryTypeList) {
    if(categoryTypeList == null || categoryTypeList.isEmpty()) {
      return new ResponseEntity<>(postService.findAllPublicPosts(), HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>(postService.findAllPublicPostsByCategories(categoryTypeList), HttpStatus.OK);
    }
  }

  // 모든 친구 공개 게시글 조회
  @GetMapping("/friends")
  public ResponseEntity<List<PostResponseDto>> findAllFriendPosts(HttpSession session) {
    Long userId = (Long)session.getAttribute("loginUser");
    return new ResponseEntity<>(postService.findAllFriendPosts(userId), HttpStatus.OK);
  }

  // 사용자의 모든 게시글 조회
  // TODO: 유저 컨트롤러로 이동 고려 "/users/{userId}/posts"
  @GetMapping("/{targetUserId}")
  public ResponseEntity<List<PostResponseDto>> findAllByUser(@PathVariable Long targetUserId, HttpSession session) {
    Long loginId = (Long)session.getAttribute("loginUser");
    return new ResponseEntity<>(postService.findAllUserPosts(targetUserId, loginId), HttpStatus.OK);
  }

  // 게시글 수정
  @PatchMapping("/{postId}")
  public ResponseEntity<Void> updatePost(
      @PathVariable Long postId, @RequestBody PostUpdateRequestDto requestDTO, HttpSession session) {
    Long loginId = (Long)session.getAttribute("loginUser");
    postService.updatePost(postId, requestDTO, loginId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // 게시글 삭제
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable Long postId, HttpSession session) {
    Long loginId = (Long)session.getAttribute("loginUser");
    postService.deletePost(postId, loginId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
