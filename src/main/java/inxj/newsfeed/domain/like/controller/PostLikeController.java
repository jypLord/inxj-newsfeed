package inxj.newsfeed.domain.like.controller;

import inxj.newsfeed.domain.like.service.PostLikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static inxj.newsfeed.common.constant.Const.LOGIN_USER;

@RequestMapping("/posts/{postId}/likes")
@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<Void> like(@PathVariable Long postId, @SessionAttribute(LOGIN_USER) Long loginUserId) {
        postLikeService.like(postId, loginUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> unlike(@PathVariable Long postId, @SessionAttribute(LOGIN_USER) Long loginUserId) {

        postLikeService.unlike(postId, loginUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}