package inxj.newsfeed.domain.friend.controller;

import inxj.newsfeed.domain.friend.dto.FriendRequestResponseDto;
import inxj.newsfeed.domain.friend.dto.FriendRequestWithStatusResponseDto;
import inxj.newsfeed.domain.friend.dto.FriendResponseDto;
import inxj.newsfeed.domain.friend.service.FriendService;
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
    public ResponseEntity<List<FriendResponseDto>> findAllFriends(@SessionAttribute("loginUser") Long userId){
        return new ResponseEntity<>(friendService.findAllFriends(userId), HttpStatus.OK);
    }

    /*
    친구 삭제 API
     */
    @DeleteMapping("/friends/{targetId}")
    public ResponseEntity<Void> deleteFriend(@SessionAttribute("loginUser") Long loginUserId, @PathVariable Long targetId){
        friendService.deleteFriend(loginUserId, targetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    친구 요청 API
     */
    @PostMapping("/friend-requests/{targetId}")
    public ResponseEntity<Void> requestFriend(@SessionAttribute("loginUser") Long loginUserId, @PathVariable Long targetId){
        friendService.requestFriend(loginUserId, targetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    친구 요청 수락 API
     */
    @PostMapping("/friend-requests/accept/{targetId}")
    public ResponseEntity<Void> acceptRequest(@SessionAttribute("loginUser") Long loginUserId, @PathVariable Long targetId){
        friendService.acceptRequest(loginUserId, targetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    친구 요청 거절 API
     */
    @PostMapping("/friend-requests/reject/{targetId}")
    public ResponseEntity<Void> rejectRequest(@SessionAttribute("loginUser") Long loginUserId, @PathVariable Long targetId){
        friendService.rejectRequest(loginUserId, targetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    보낸 친구 요청 목록 조회 API
     */
    @GetMapping("/friend-requests/sent")
    public ResponseEntity<List<FriendRequestWithStatusResponseDto>> findSentRequests(@SessionAttribute("loginUser") Long userId){
        return new ResponseEntity<>(friendService.findSentRequests(userId), HttpStatus.OK);
    }

    /*
    받은 친구 요청 목록 조회 API
     */
    @GetMapping("/friend-requests/received")
    public ResponseEntity<List<FriendRequestResponseDto>> findReceivedRequests(@SessionAttribute("loginUser") Long userId){
        return new ResponseEntity<>(friendService.findReceivedRequests(userId), HttpStatus.OK);
    }
}