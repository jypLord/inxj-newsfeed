package inxj.newsfeed.friend.controller;

import inxj.newsfeed.friend.dto.FriendRequestResponseDto;
import inxj.newsfeed.friend.dto.FriendResponseDto;
import inxj.newsfeed.friend.service.FriendService;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<List<FriendResponseDto>> findAllFriends(HttpSession session){
        // 로그인한 유저의 id 뽑아오기
        Long userId = (Long) session.getAttribute("loginUser");

        return new ResponseEntity<>(friendService.findAllFriends(userId), HttpStatus.OK);
    }

    /*
    친구 삭제 API
     */
    @DeleteMapping("/friends/{friendId}")
    public ResponseEntity<Void> deleteFriend(HttpSession session, @PathVariable Long friendId){
        // 로그인한 유저의 id 뽑아오기
        Long loginUserId = (Long) session.getAttribute("loginUser");

        // 로그인한 유저의 id와 삭제할 친구의 id를 서비스 레이어에 전달
        friendService.deleteFriend(loginUserId, friendId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    친구 요청 API
     */
    @PostMapping("/friend-requests/{userId}")
    public ResponseEntity<Void> requestFriend(HttpSession session, @PathVariable Long userId){
        // 로그인한 유저의 id 뽑아오기
        Long loginUserId = (Long) session.getAttribute("loginUser");

        // 로그인한 유저의 id와 친구 요청할 유저의 id를 서비스 레이어에 전달
        friendService.requestFriend(loginUserId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    친구 요청 수락 API
     */
    @PostMapping("/friend-requests/accept/{userId}")
    public ResponseEntity<Void> acceptRequest(HttpSession session, @PathVariable Long userId){
        // 로그인한 유저의 id 뽑아오기
        Long loginUserId = (Long) session.getAttribute("loginUser");

        // 로그인한 유저의 id와 요청 수락할 유저의 id를 서비스 레이어에 전달
        friendService.acceptRequest(loginUserId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    친구 요청 거절 API
     */
    @PostMapping("/friend-requests/reject/{userId}")
    public ResponseEntity<Void> rejectRequest(HttpSession session, @PathVariable Long userId){
        // 로그인한 유저의 id 뽑아오기
        Long loginUserId = (Long) session.getAttribute("loginUser");

        // 로그인한 유저의 id와 요청 거절할 유저의 id를 서비스 레이어에 전달
        friendService.rejectRequest(loginUserId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    보낸 친구 요청 목록 조회 API
     */
    @GetMapping("/friend-requests/sent")
    public ResponseEntity<List<FriendRequestResponseDto>> findSentRequests(HttpSession session){
        // 로그인한 유저의 id 뽑아오기
        Long userId = (Long) session.getAttribute("loginUser");

        return new ResponseEntity<>(friendService.findSentRequests(userId), HttpStatus.OK);
    }

    /*
    받은 친구 요청 목록 조회 API
     */
    @GetMapping("/friend-requests/received")
    public ResponseEntity<List<FriendRequestResponseDto>> findReceivedRequests(HttpSession session){
        // 로그인한 유저의 id 뽑아오기
        Long userId = (Long) session.getAttribute("loginUser");

        return new ResponseEntity<>(friendService.findReceivedRequests(userId), HttpStatus.OK);
    }
}
