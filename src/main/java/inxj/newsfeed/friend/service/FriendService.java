package inxj.newsfeed.friend.service;

import inxj.newsfeed.exception.CustomException;
import inxj.newsfeed.friend.dto.FriendRequestResponseDto;
import inxj.newsfeed.friend.dto.FriendResponseDto;
import inxj.newsfeed.friend.entity.FriendRequest;
import inxj.newsfeed.friend.repository.FriendRepository;
import inxj.newsfeed.user.entity.User;
import inxj.newsfeed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static inxj.newsfeed.exception.ErrorCode.*;
import static inxj.newsfeed.friend.entity.Status.*;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    /*
    친구 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. 조회한 사용자의 친구 요청 목록에서 Status가 Accept인 사용자 조회
     */
    public List<FriendResponseDto> findAllFriends(Long userId) {
        // User 가져오기
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        // user가 receiver or requester인 데이터 중에 status == Accept인 데이터 조회
        List<User> foundFriends = friendRepository.findByUserAndStatus(user, ACCEPT);

        return foundFriends.stream()
                .map(FriendResponseDto::new)
                .toList();
    }

    /*
    친구 삭제 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회(두 사용자의 위치가 뒤바뀐 데이터도 조회)
    3. Status 값 변경
     */
    @Transactional
    public void deleteFriend(Long loginUserId, Long friendId) {
        // User 가져오기
        User loginUser = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(INVALID_USER_ID));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        // 두 사용자가 requester or receiver인 friendRequest 반환
        FriendRequest foundFriendRequest = friendRepository.findInteractiveRequest(loginUser, friend).orElseThrow(() -> new CustomException(INVALID_FRIEND_REQUEST));

        // Status가 Accept인 경우 DELETED로 변경
        if (foundFriendRequest.getStatus() == ACCEPT) {
            foundFriendRequest.setStatus(DELETED);
        }else{
            // Status가 Accept가 아닌 경우 예외 발생
            throw new CustomException(INVALID_FRIEND_REQUEST);
        }
    }

    /*
    친구 요청 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2-1. friendRequest 테이블에 데이터가 삽입된 적이 없으면 insert
    2-2. friendRequest 테이블에 이미 저장되어있는 데이터인 경우, Status가 DELETE or REJECT인 경우 PENDING으로 변경
     */
    @Transactional
    public void requestFriend(Long loginUserId, Long userId) {
        // User 가져오기
        User requester = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(INVALID_USER_ID));
        User receiver = userRepository.findById(userId).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        // FriendRequest에서 데이터 조회
        Optional<FriendRequest> foundFriendRequest = friendRepository.findInteractiveRequest(requester, receiver);

        // insert
        if (foundFriendRequest.isEmpty()) {
            friendRepository.save(new FriendRequest(requester, receiver));
        } // Status PENDING으로 변경
        else if (foundFriendRequest.get().getStatus() == REJECT
                || foundFriendRequest.get().getStatus() == DELETED) {
            foundFriendRequest.get().setStatus(PENDING);
        }else{
            // Status가 PENDING or ACCEPT인 경우 예외 발생
            throw new CustomException(CONFLICT_STATUS);
        }
    }

    /*
    친구 요청 수락 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
    3. Status 값 변경
     */
    @Transactional
    public void acceptRequest(Long loginUserId, Long userId) {
        // 전달받은 id값을 가지는 사용자가 있는지 확인
        User receiver = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(INVALID_USER_ID));
        User requester = userRepository.findById(userId).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        // 상대 유저가 requester, 로그인 유저가 receiver인 friendRequest 반환
        FriendRequest foundFriendRequest = friendRepository.findByReceiverAndRequester(receiver, requester).orElseThrow(() -> new CustomException(INVALID_FRIEND_REQUEST));

        // Status가 PENDING 상태인 경우 ACCEPT로 변경
        if (foundFriendRequest.getStatus() == PENDING) {
            foundFriendRequest.setStatus(ACCEPT);
        }else {
            // Status가 PENDING이 아닌 경우 예외 발생(이미 요청을 거절, 수락하거나 친구 삭제한 경우)
            throw new CustomException(INVALID_FRIEND_REQUEST);
        }
    }

    /*
     친구 요청 거절 API
     1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
     2. FriendRequest 테이블에서 데이터 조회
     3. Status 값 변경
      */
    @Transactional
    public void rejectRequest(Long loginUserId, Long userId) {
        // User 가져오기
        User receiver = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(INVALID_USER_ID));
        User requester = userRepository.findById(userId).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        // 상대 유저가 requester, 로그인 유저가 receiver인 friendRequest 반환
        FriendRequest foundFriendRequest = friendRepository.findByReceiverAndRequester(receiver, requester).orElseThrow(() -> new CustomException(INVALID_FRIEND_REQUEST));

        // Status가 PENDING 상태인 경우 REJECT로 변경
        if (foundFriendRequest.getStatus() == PENDING) {
            foundFriendRequest.setStatus(REJECT);
        }else{
            // Status가 PENDING이 아닌 경우 예외 발생(이미 요청을 거절, 수락하거나 친구 삭제한 경우)
            throw new CustomException(INVALID_FRIEND_REQUEST);
        }
    }

    /*
    보낸 친구 요청 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
     */
    public List<FriendRequestResponseDto> findSentRequests(Long userId) {
        // User 가져오기
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        // user가 requester인 friendRequest 반환
        List<FriendRequest> foundRequests = friendRepository.findByRequester(user);

        return foundRequests.stream()
                .map(FriendRequestResponseDto::new)
                .toList();
    }

    /*
    받은 친구 요청 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
     */
    public List<FriendRequestResponseDto> findReceivedRequests(Long userId) {
        // User 가져오기
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        // user가 receiver인 friendRequest 반환
        List<FriendRequest> foundRequests = friendRepository.findByReceiver(user);

        return foundRequests.stream()
                .map(FriendRequestResponseDto::new)
                .toList();
    }
}
