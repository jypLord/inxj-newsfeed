package inxj.newsfeed.domain.like.controller;

import inxj.newsfeed.domain.like.service.CommentLikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static inxj.newsfeed.common.constant.Const.LOGIN_USER;

@RequestMapping("/posts/{postId}/comments/{commentId}/likes")
@RestController
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<Void> like(@PathVariable Long commentId,
                                     @SessionAttribute(LOGIN_USER) Long loginUserId) {


        commentLikeService.like(commentId,loginUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> unlike(@PathVariable Long commentId,@SessionAttribute(LOGIN_USER) Long loginUserId) {
        commentLikeService.unlike(commentId, loginUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
