package inxj.newsfeed.like.controller;

import inxj.newsfeed.like.service.CommentLikeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts/{postId}/comments/{commentId}/likes")
@RestController
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @Autowired
    public CommentLikeController(CommentLikeService commentLikeService) {
        this.commentLikeService = commentLikeService;
    }

    @PostMapping
    public ResponseEntity<Void> like(@PathVariable Long commentId, HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUser"); // 로그인한 유저의 ID(FK)를 세션에서 가져옴
        commentLikeService.like(commentId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> unlike(@PathVariable Long commentId, HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUser"); // 로그인한 유저의 ID(FK)를 세션에서 가져옴
        commentLikeService.unlike(commentId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
