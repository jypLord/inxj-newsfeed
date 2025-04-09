package inxj.newsfeed.friend.service;

import inxj.newsfeed.friend.dto.FriendRequestResponseDto;
import inxj.newsfeed.friend.dto.FriendResponseDto;
import inxj.newsfeed.friend.entity.FriendRequest;
import inxj.newsfeed.friend.repository.FriendRepository;
import inxj.newsfeed.user.User;
import inxj.newsfeed.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static inxj.newsfeed.friend.entity.Status.*;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

     // Todo: 예외처리
     // NoSuchElementException -> 404 Not Found / "조회한 데이터가 존재하지 않습니다."
     // NotFriendException -> 400 Bad Request / "해당 유저와 친구 관계가 아닙니다."
     // AlreadyProcessedException -> 409 Conflict / "이미 처리된 요청입니다." / 현재 Status 파라미터로 전달

    /*
    친구 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. 조회한 사용자의 친구 요청 목록에서 Status가 Accept인 사용자 조회
     */
    public List<FriendResponseDto> findAllFriends(Long userId) {
        // User 가져오기 (UserRepository 의존)
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        // user가 receiver or requester인 데이터 중에 status == Accept인 데이터 조회
        List<User> foundFriends = friendRepository.findByRequesterOrReceiverAndStatus(user, user, ACCEPT);

        // foundFriends를 responseDtos에 삽입
        List<FriendResponseDto> responseDtos = foundFriends.stream()
                .map(FriendResponseDto::new)
                .toList();

        return responseDtos;
    }

    /*
    친구 삭제 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회(두 사용자의 위치가 뒤바뀐 데이터도 조회)
    3. Status 값 변경
     */
    @Transactional
    public void deleteFriend(Long loginedUserId, Long friendId) {
        // User 가져오기 (UserRepository 의존)
        User loginedUser = userRepository.findById(loginedUserId).orElseThrow(NoSuchElementException::new);
        User friend = userRepository.findById(friendId).orElseThrow(NoSuchElementException::new);

        // 두 사용자가 requester or receiver인 friendRequest 반환
        FriendRequest foundFriendRequest = friendRepository.findInteractiveRequest(loginedUser, friend).orElseThrow(NoSuchElementException::new);

        // Status가 Accept인 경우 DELETED로 변경
        if (foundFriendRequest.getStatus() == ACCEPT) {
            foundFriendRequest.setStatus(DELETED);
        }else{
            // Status가 Accept가 아닌 경우 예외 발생
            throw new NotFriendException();
        }
    }

    /*
    친구 요청 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2-1. friendRequest 테이블에 데이터가 삽입된 적이 없으면 insert
    2-2. friendRequest 테이블에 이미 저장되어있는 데이터인 경우, Status가 DELETE or REJECT인 경우 PENDING으로 변경
     */
    @Transactional
    public void requestFriend(Long loginedUserId, Long userId) {
        // User 가져오기 (UserRepository 의존)
        User requester = userRepository.findById(loginedUserId).orElseThrow(NoSuchElementException::new);
        User receiver = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

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
            throw new AlreadyProcessedException();
        }
    }

    /*
    친구 요청 수락 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
    3. Status 값 변경
     */
    @Transactional
    public void acceptRequest(Long loginedUserId, Long userId) {
        // 전달받은 id값을 가지는 사용자가 있는지 확인 (UserRepository 의존)
        User receiver = userRepository.findById(loginedUserId).orElseThrow(NoSuchElementException::new);
        User requester = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        // 상대 유저가 requester, 로그인 유저가 receiver인 friendRequest 반환
        FriendRequest foundFriendRequest = friendRepository.findByReceiverAndRequester(receiver, requester).orElseThrow(NoSuchElementException::new);

        // Status가 PENDING 상태인 경우 ACCEPT로 변경
        if (foundFriendRequest.getStatus() == PENDING) {
            foundFriendRequest.setStatus(ACCEPT);
        }else {
            // Status가 PENDING이 아닌 경우 예외 발생(이미 요청을 거절, 수락하거나 친구 삭제한 경우)
            throw new AlreadyProcessedException();
        }
    }

    /*
     친구 요청 거절 API
     1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
     2. FriendRequest 테이블에서 데이터 조회
     3. Status 값 변경
      */
    @Transactional
    public void rejectRequest(Long loginedUserId, Long userId) {
        // User 가져오기 (UserRepository 의존)
        User receiver = userRepository.findById(loginedUserId).orElseThrow(NoSuchElementException::new);
        User requester = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        // 상대 유저가 requester, 로그인 유저가 receiver인 friendRequest 반환
        FriendRequest foundFriendRequest = friendRepository.findByReceiverAndRequester(receiver, requester).orElseThrow(NoSuchElementException::new);

        // Status가 PENDING 상태인 경우 REJECT로 변경
        if (foundFriendRequest.getStatus() == PENDING) {
            foundFriendRequest.setStatus(REJECT);
        }else{
            // Status가 PENDING이 아닌 경우 예외 발생(이미 요청을 거절, 수락하거나 친구 삭제한 경우)
            throw new AlreadyProcessedException();
        }
    }

    /*
    보낸 친구 요청 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
     */
    public List<FriendRequestResponseDto> findSentRequests(Long userId) {
        // User 가져오기 (UserRepository 의존)
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        // user가 requester인 friendRequest 반환
        List<FriendRequest> foundRequests = friendRepository.findByRequester(user);

        // foundRequests를 responseDtos에 삽입
        List<FriendRequestResponseDto> responseDtos = foundRequests.stream()
                .map(FriendRequestResponseDto::new)
                .toList();

        return responseDtos;
    }

    /*
    받은 친구 요청 목록 조회 API
    1. 전달받은 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 데이터 조회
     */
    public List<FriendRequestResponseDto> findReceivedRequests(Long userId) {
        // User 가져오기 (UserRepository 의존)
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        // user가 receiver인 friendRequest 반환
        List<FriendRequest> foundRequests = friendRepository.findByReceiver(user);

        // foundRequests를 responseDtos에 삽입
        List<FriendRequestResponseDto> responseDtos = foundRequests.stream()
                .map(FriendRequestResponseDto::new)
                .toList();

        return responseDtos;
    }
}
