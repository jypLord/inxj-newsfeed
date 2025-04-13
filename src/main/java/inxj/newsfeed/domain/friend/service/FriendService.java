package inxj.newsfeed.domain.friend.service;

import inxj.newsfeed.common.util.EntityFetcher;
import inxj.newsfeed.exception.BaseException;
import inxj.newsfeed.domain.friend.dto.FriendRequestResponseDto;
import inxj.newsfeed.domain.friend.dto.FriendRequestWithStatusResponseDto;
import inxj.newsfeed.domain.friend.dto.FriendResponseDto;
import inxj.newsfeed.domain.friend.entity.FriendRequest;
import inxj.newsfeed.domain.friend.repository.FriendRepository;
import inxj.newsfeed.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static inxj.newsfeed.exception.ErrorCode.*;
import static inxj.newsfeed.domain.friend.entity.Status.*;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final EntityFetcher entityFetcher;

    /*
    친구 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. 조회한 사용자가 requester or receiver인 친구 요청 목록에서 Status가 ACCEPT인 사용자 조회
     */
    public List<FriendResponseDto> findAllFriends(Long userId) {
        User user = entityFetcher.getUserOrThrow(userId);

        return friendRepository.findByUserAndStatus(user, ACCEPT).stream()
                // FriendRequest -> User
                .map(friendRequest ->
                        friendRequest.getReceiver().equals(user) ? friendRequest.getRequester() : friendRequest.getReceiver())
                // User -> Dto
                .map(friend -> new FriendResponseDto(
                        friend.getUsername(),
                        friend.getName(),
                        friend.getProfileImageUrl()))
                .toList();
    }

    /*
    친구 삭제 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회(두 사용자의 위치가 뒤바뀐 데이터도 조회)
    3. Status 변경
     */
    @Transactional
    public void deleteFriend(Long loginUserId, Long targetId) {
        User loginUser = entityFetcher.getUserOrThrow(loginUserId);
        User targetUser = entityFetcher.getUserOrThrow(targetId);

        FriendRequest foundFriendRequest = entityFetcher.getInteractiveFriendRequestOrThrow(loginUser, targetUser);

        if (foundFriendRequest.getStatus() == ACCEPT) {
            foundFriendRequest.setStatus(DELETED);
        }else{
            throw new BaseException(INVALID_FRIEND_REQUEST);
        }
    }

    /*
    친구 요청 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2-0. loginUser == targetUser 예외처리
    2-1. friendRequest 테이블에 데이터가 삽입된 적이 없으면 insert
    2-2. 상대방이 이미 나한테 친구 요청을 보낸 상태면 Status를 ACCEPT로 변경
    2-3. friendRequest 테이블에 이미 저장되어있으면, Status가 DELETE or REJECT인 경우에만 PENDING으로 변경
     */
    @Transactional
    public void requestFriend(Long loginUserId, Long targetId) {
        User loginUser = entityFetcher.getUserOrThrow(loginUserId);
        User targetUser = entityFetcher.getUserOrThrow(targetId);

        if(loginUser.equals(targetUser)){
            throw new BaseException(INVALID_FRIEND_REQUEST);
        }

        Optional<FriendRequest> foundFriendRequest = friendRepository.findInteractiveRequest(loginUser, targetUser);

        if (foundFriendRequest.isEmpty()) {
            friendRepository.save(new FriendRequest(loginUser, targetUser));
        }
        else if(foundFriendRequest.get().getRequester().equals(targetUser) && foundFriendRequest.get().getStatus() == PENDING){
            foundFriendRequest.get().setStatus(ACCEPT);
        }
        else if (foundFriendRequest.get().getStatus() == REJECT
                || foundFriendRequest.get().getStatus() == DELETED) {
            foundFriendRequest.get().setStatus(PENDING);
        }else{
            throw new BaseException(CONFLICT_STATUS);
        }
    }

    /*
    친구 요청 수락 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
    3. Status 변경
     */
    @Transactional
    public void acceptRequest(Long loginUserId, Long targetId) {
        User receiver = entityFetcher.getUserOrThrow(loginUserId);
        User requester = entityFetcher.getUserOrThrow(targetId);

        FriendRequest foundFriendRequest = entityFetcher.getFriendRequestOrThrow(receiver, requester);

        if (foundFriendRequest.getStatus() == PENDING) {
            foundFriendRequest.setStatus(ACCEPT);
        }else {
            throw new BaseException(INVALID_FRIEND_REQUEST);
        }
    }

    /*
     친구 요청 거절 API
     1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
     2. FriendRequest 테이블에서 데이터 조회
     3. Status 변경
      */
    @Transactional
    public void rejectRequest(Long loginUserId, Long targetId) {
        User receiver = entityFetcher.getUserOrThrow(loginUserId);
        User requester = entityFetcher.getUserOrThrow(targetId);

        FriendRequest foundFriendRequest = entityFetcher.getFriendRequestOrThrow(receiver, requester);

        if (foundFriendRequest.getStatus() == PENDING) {
            foundFriendRequest.setStatus(REJECT);
        }else{
            throw new BaseException(INVALID_FRIEND_REQUEST);
        }
    }

    /*
    보낸 친구 요청 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 Status가 PENDING or REJECT인 데이터 조회
     */
    public List<FriendRequestWithStatusResponseDto> findSentRequests(Long userId) {
        User user = entityFetcher.getUserOrThrow(userId);

        List<FriendRequest> foundRequests = friendRepository.findByRequesterAndStatusIn(user, List.of(PENDING, REJECT));

        return foundRequests.stream()
                .map(foundRequest -> new FriendRequestWithStatusResponseDto(
                        foundRequest.getReceiver().getUsername(),
                        foundRequest.getReceiver().getName(),
                        foundRequest.getReceiver().getProfileImageUrl(),
                        foundRequest.getStatus()))
                .toList();
    }

    /*
    받은 친구 요청 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 Status가 PENDING인 데이터 조회
     */
    public List<FriendRequestResponseDto> findReceivedRequests(Long userId) {
        User user = entityFetcher.getUserOrThrow(userId);

        List<FriendRequest> foundRequests = friendRepository.findByReceiverAndStatus(user, PENDING);

        return foundRequests.stream()
                .map(foundRequest -> new FriendRequestResponseDto(
                        foundRequest.getRequester().getUsername(),
                        foundRequest.getRequester().getName(),
                        foundRequest.getRequester().getProfileImageUrl()))
                .toList();
    }
}
