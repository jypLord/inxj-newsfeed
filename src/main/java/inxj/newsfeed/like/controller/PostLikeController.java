package inxj.newsfeed.like.controller;

import inxj.newsfeed.like.service.PostLikeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts/{postId}/likes")
@RestController
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Autowired
    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping
    public ResponseEntity<Void> like(@PathVariable Long postId, HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUser"); // 로그인한 유저의 ID(FK)를 세션에서 가져옴
        postLikeService.like(postId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> unlike(@PathVariable Long postId, HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUser"); // 로그인한 유저의 ID(FK)를 세션에서 가져옴
        postLikeService.unlike(postId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}