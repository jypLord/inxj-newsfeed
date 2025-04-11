package inxj.newsfeed.domain.friend.service;

import inxj.newsfeed.common.util.EntityFetcher;
import inxj.newsfeed.exception.CustomException;
import inxj.newsfeed.domain.friend.dto.FriendRequestResponseDto;
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
    public void deleteFriend(Long loginUserId, Long friendId) {
        User loginUser = entityFetcher.getUserOrThrow(loginUserId);
        User friend = entityFetcher.getUserOrThrow(friendId);

        FriendRequest foundFriendRequest = entityFetcher.getInteractiveFriendRequestOrThrow(loginUser, friend);

        if (foundFriendRequest.getStatus() == ACCEPT) {
            foundFriendRequest.setStatus(DELETED);
        }else{
            throw new CustomException(INVALID_FRIEND_REQUEST);
        }
    }

    /*
    친구 요청 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2-1. friendRequest 테이블에 데이터가 삽입된 적이 없으면 insert
    2-2. friendRequest 테이블에 이미 저장되어있으면, Status가 DELETE or REJECT인 경우에만 PENDING으로 변경
     */
    @Transactional
    public void requestFriend(Long loginUserId, Long userId) {
        User requester = entityFetcher.getUserOrThrow(loginUserId);
        User receiver = entityFetcher.getUserOrThrow(userId);

        Optional<FriendRequest> foundFriendRequest = friendRepository.findInteractiveRequest(requester, receiver);

        if (foundFriendRequest.isEmpty()) {
            friendRepository.save(new FriendRequest(requester, receiver));
        }
        else if (foundFriendRequest.get().getStatus() == REJECT
                || foundFriendRequest.get().getStatus() == DELETED) {
            foundFriendRequest.get().setStatus(PENDING);
        }else{
            throw new CustomException(CONFLICT_STATUS);
        }
    }

    /*
    친구 요청 수락 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
    3. Status 변경
     */
    @Transactional
    public void acceptRequest(Long loginUserId, Long userId) {
        User receiver = entityFetcher.getUserOrThrow(loginUserId);
        User requester = entityFetcher.getUserOrThrow(userId);

        FriendRequest foundFriendRequest = entityFetcher.getFriendRequestOrThrow(receiver, requester);

        if (foundFriendRequest.getStatus() == PENDING) {
            foundFriendRequest.setStatus(ACCEPT);
        }else {
            throw new CustomException(INVALID_FRIEND_REQUEST);
        }
    }

    /*
     친구 요청 거절 API
     1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
     2. FriendRequest 테이블에서 데이터 조회
     3. Status 변경
      */
    @Transactional
    public void rejectRequest(Long loginUserId, Long userId) {
        User receiver = entityFetcher.getUserOrThrow(loginUserId);
        User requester = entityFetcher.getUserOrThrow(userId);

        FriendRequest foundFriendRequest = entityFetcher.getFriendRequestOrThrow(receiver, requester);

        if (foundFriendRequest.getStatus() == PENDING) {
            foundFriendRequest.setStatus(REJECT);
        }else{
            throw new CustomException(INVALID_FRIEND_REQUEST);
        }
    }

    /*
    보낸 친구 요청 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
     */
    public List<FriendRequestResponseDto> findSentRequests(Long userId) {
        User user = entityFetcher.getUserOrThrow(userId);

        List<FriendRequest> foundRequests = friendRepository.findByRequester(user);

        return foundRequests.stream()
                .map(foundRequest -> new FriendRequestResponseDto(
                        foundRequest.getReceiver().getUsername(),
                        foundRequest.getReceiver().getName(),
                        foundRequest.getReceiver().getProfileImageUrl()))
                .toList();
    }

    /*
    받은 친구 요청 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
     */
    public List<FriendRequestResponseDto> findReceivedRequests(Long userId) {
        User user = entityFetcher.getUserOrThrow(userId);

        List<FriendRequest> foundRequests = friendRepository.findByReceiver(user);

        return foundRequests.stream()
                .map(foundRequest -> new FriendRequestResponseDto(
                        foundRequest.getRequester().getUsername(),
                        foundRequest.getRequester().getName(),
                        foundRequest.getRequester().getProfileImageUrl()))
                .toList();
    }
}
