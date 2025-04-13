package inxj.newsfeed.domain.comment.controller;

import inxj.newsfeed.domain.comment.dto.RequestDto;
import inxj.newsfeed.domain.comment.dto.ResponseDto;
import inxj.newsfeed.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentControl {

    private final CommentService commentService;

    // 생성
    @PostMapping()
    public ResponseEntity<Void> saveComment(
            @RequestBody @Valid RequestDto requestDto,
            @PathVariable Long postId,
            @SessionAttribute(name = "loginUser") Long userId) {


        commentService.saveComment(userId, postId, requestDto);

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
            @RequestBody @Valid RequestDto requestDto,
            @SessionAttribute(name = "loginUser") Long userId) {


        commentService.updateComment(userId, commentId, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @SessionAttribute(name = "loginUser") Long userId) {


        commentService.deleteComment(userId, commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
