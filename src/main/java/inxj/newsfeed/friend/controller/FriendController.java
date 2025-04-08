package inxj.newsfeed.friend.controller;

import inxj.newsfeed.friend.dto.FriendResponseDto;
import inxj.newsfeed.friend.service.FriendService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @GetMapping("/friends")
    public ResponseEntity<List<FriendResponseDto>> findAllFriends(HttpServletRequest request){
        // 로그인한 유저의 id를 뽑아오기
        Long userId = (Long)request.getAttribute("userId");

        return new ResponseEntity<>(friendService.findAllFriends(userId), HttpStatus.OK);
    }
}
