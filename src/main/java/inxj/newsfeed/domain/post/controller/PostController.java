package inxj.newsfeed.domain.post.controller;

import inxj.newsfeed.domain.post.dto.PostCreateRequestDto;
import inxj.newsfeed.domain.post.dto.PostResponseDto;
import inxj.newsfeed.domain.post.dto.PostUpdateRequestDto;
import inxj.newsfeed.domain.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static inxj.newsfeed.common.constant.Const.LOGIN_USER;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
@Validated
public class PostController {
  private final PostService postService;

  // 게시글 생성
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody @Valid PostCreateRequestDto requestDTO, @SessionAttribute(LOGIN_USER) Long loginUserId) {

    postService.save(requestDTO, loginUserId);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  // 게시글 단건 조회
  @GetMapping("/{postId}")
  public ResponseEntity<PostResponseDto> find(@PathVariable Long postId, @SessionAttribute(LOGIN_USER) Long loginUserId) {

    return new ResponseEntity<>(postService.find(postId,loginUserId), HttpStatus.OK);
  }

  // 모든 전체 공개 게시글 조회
  // 조건: 전체, 카테고리
  @GetMapping
  public ResponseEntity<List<PostResponseDto>> findAllPublicPosts(
      @RequestParam(name = "categoryType", required = false) List<String> categoryTypeList,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    if(categoryTypeList == null || categoryTypeList.isEmpty()) {
      return new ResponseEntity<>(postService.findAllPublicPosts(page, size), HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>(postService.findAllPublicPostsByCategories(categoryTypeList, page, size), HttpStatus.OK);
    }
  }

  // 모든 친구 공개 게시글 조회
  @GetMapping("/friends")
  public ResponseEntity<List<PostResponseDto>> findAllFriendPosts(
      @SessionAttribute(LOGIN_USER) Long loginUserId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {

    return new ResponseEntity<>(postService.findAllFriendPosts(loginUserId, page, size), HttpStatus.OK);
  }

  // 사용자의 모든 게시글 조회
  @GetMapping("/users/{targetUserId}")
  public ResponseEntity<List<PostResponseDto>> findAllByUser(
      @PathVariable Long targetUserId, @SessionAttribute(LOGIN_USER) Long loginUserId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {

    return new ResponseEntity<>(postService.findAllUserPosts(targetUserId, loginUserId, page, size), HttpStatus.OK);
  }

  // 게시글 수정
  @PatchMapping("/{postId}")
  public ResponseEntity<Void> updatePost(
      @PathVariable Long postId, @RequestBody PostUpdateRequestDto requestDTO, @SessionAttribute(LOGIN_USER) Long loginUserId) {

    postService.updatePost(postId, requestDTO, loginUserId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // 게시글 삭제
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable Long postId, @SessionAttribute(LOGIN_USER) Long loginUserId) {

    postService.deletePost(postId, loginUserId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
