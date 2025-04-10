package inxj.newsfeed.comment.controller;

import inxj.newsfeed.comment.dto.RequestDto;
import inxj.newsfeed.comment.dto.ResponseDto;
import inxj.newsfeed.comment.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentControl {

    private final CommentService commentService;

    // 생성
    @PostMapping()
    public ResponseEntity<Void> saveComment(
            @RequestBody RequestDto requestDto,
            @PathVariable Long postId,
            HttpSession session) {
        Long id = (Long) session.getAttribute("loginUser");

        commentService.saveComment(id, postId, requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 조회
    @GetMapping()
    public ResponseEntity<List<ResponseDto>> findComment(@PathVariable Long postId) {
        return new ResponseEntity<>(commentService.findComment(postId), HttpStatus.OK);
    }

    // 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @RequestBody RequestDto requestDto,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUser");

        commentService.updateComment(userId, commentId, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUser");

        commentService.deleteComment(userId, commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
