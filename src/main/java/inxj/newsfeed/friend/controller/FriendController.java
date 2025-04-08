package inxj.newsfeed.friend.controller;

import inxj.newsfeed.friend.dto.FriendResponseDto;
import inxj.newsfeed.friend.service.FriendService;
import inxj.newsfeed.user.User;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    /*
    친구 목록 조회 API
     */
    @GetMapping("/friends")
    public ResponseEntity<List<FriendResponseDto>> findAllFriends(HttpServletRequest request){
        // 로그인한 유저의 id 뽑아오기
        Long userId = (Long)request.getAttribute("userId");

        return new ResponseEntity<>(friendService.findAllFriends(userId), HttpStatus.OK);
    }

    /*
    친구 삭제 API
     */
    @PatchMapping("/friends/{friendId}")
    public ResponseEntity<Void> deleteFriend(HttpServletRequest request, @PathVariable Long friendId){
        // 로그인한 유저의 id 뽑아오기
        Long loginedUserId = (Long) request.getAttribute("userId");

        // 로그인한 유저의 id와 삭제할 친구의 id를 서비스 레이어에 전달
        friendService.deleteFriend(loginedUserId, friendId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    친구 요청 API
     */
    @PostMapping("/friend-request/{userId}")
    public ResponseEntity<Void> requestFriend(HttpServletRequest request, @PathVariable Long userId){
        // 로그인한 유저의 id 뽑아오기
        Long loginedUserId = (Long) request.getAttribute("userId");

        // 로그인한 유저의 id와 친구 요청할 유저의 id를 서비스 레이어에 전달
        friendService.requestFriend(loginedUserId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    친구 요청 수락 API
     */
    @PostMapping("/friend-request/accept/{userId}")
    public ResponseEntity<Void> acceptRequest(HttpServletRequest request, @PathVariable Long userId){
        // 로그인한 유저의 id 뽑아오기
        Long loginedUserId = (Long) request.getAttribute("userId");

        // 로그인한 유저의 id와 요청 수락할 유저의 id를 서비스 레이어에 전달
        friendService.acceptRequest(loginedUserId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
