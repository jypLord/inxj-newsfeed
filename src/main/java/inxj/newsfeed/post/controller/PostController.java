package inxj.newsfeed.post.controller;

import inxj.newsfeed.post.dto.PostCreateRequestDTO;
import inxj.newsfeed.post.dto.PostResponseDTO;
import inxj.newsfeed.post.entity.Category;
import inxj.newsfeed.post.entity.CategoryType;
import inxj.newsfeed.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<Void> create(@RequestBody PostCreateRequestDTO requestDTO, HttpSession session) {
    Long userId = (Long)session.getAttribute("loginUser");
    postService.save(requestDTO, userId);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  // 게시글 단건 조회
  @GetMapping("/{id}")
  public ResponseEntity<PostResponseDTO> find(@PathVariable Long id, HttpSession session) {
    Long userId = (Long)session.getAttribute("loginUser");
    return new ResponseEntity<>(postService.find(id, userId), HttpStatus.OK);
  }

  // 모든 전체 공개 게시글 조회
  // 조건: 전체, 카테고리
  // TODO: Category List 검증 로직 필요
//  @GetMapping
//  public ResponseEntity<List<PostResponseDTO>> findAllPublicPosts(
//      @RequestParam(required = false) List<CategoryType> categories, HttpSession session) {
//    if(categories == null || categories.isEmpty()) {
//      return new ResponseEntity<>(postService.findAllPublicPosts(), HttpStatus.OK);
//    }
//    else {
//      return new ResponseEntity<>(postService.findAllPublicPostsByCategories(categories), HttpStatus.OK);
//    }
//  }

  // 모든 친구 공개 게시글 조회
  @GetMapping("/friends")
  public ResponseEntity<List<PostResponseDTO>> findAllFriendPosts(HttpSession session) {
    Long userId = (Long)session.getAttribute("loginUser");
    return new ResponseEntity<>(postService.findAllFriendPosts(userId), HttpStatus.OK);
  }

  // 사용자의 모든 게시글 조회
  // TODO: 유저 컨트롤러로 이동 고려 "/users/{userId}/posts"
  @GetMapping("/users")
  public ResponseEntity<List<PostResponseDTO>> findAllByUser(@PathVariable Long targetUserId, HttpSession session) {
    Long loginId = (Long)session.getAttribute("loginUser");
    return new ResponseEntity<>(postService.findAllUserPosts(targetUserId, loginId), HttpStatus.OK);
  }

  // 게시글 수정



  // 게시글 삭제
}
