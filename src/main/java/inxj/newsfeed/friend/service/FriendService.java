package inxj.newsfeed.friend.service;

import inxj.newsfeed.friend.dto.FriendResponseDto;
import inxj.newsfeed.friend.entity.FriendRequest;
import inxj.newsfeed.friend.repository.FriendRepository;
import inxj.newsfeed.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static inxj.newsfeed.friend.entity.Status.*;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // Todo: user가 null인 경우(NoSuchElementException) 예외처리
    // Todo: 각 API의 조건문에 해당하지 않는 경우 어떻게 처리할건지(예외? 아님 무시)

    /*
    친구 목록 조회 API
    1. 해당 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. 조회한 사용자의 친구 요청 목록에서 Status가 Accept인 사용자 조회
     */
    public List<FriendResponseDto> findAllFriends(Long userId) {
        // 전달받은 id를 가지는 User 가져오기 (UserRepository 의존)
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        // user가 receiver or requester에 있는 데이터 중에 status == Accept인 데이터 조회
        List<User> foundFriends = friendRepository.findByRequesterOrReceiverAndStatus(user, user, ACCEPT);

        List<FriendResponseDto> responseDtos = new ArrayList<>();

        // foundFriends를 ResponseDtos에 삽입
        for (User friend : foundFriends) {
            responseDtos.add(new FriendResponseDto(friend));
        }

        return responseDtos;
    }

    /*
    친구 삭제 API
    1. 해당 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 두 사용자의 데이터 조회
    3. Status 값 변경
     */
    @Transactional
    public void deleteFriend(Long loginedUserId, Long friendId) {
        // 전달받은 id값을 가지는 사용자가 있는지 확인 (UserRepository 의존)
        User loginedUser = userRepository.findById(loginedUserId).orElseThrow(NoSuchElementException::new);
        User friend = userRepository.findById(friendId).orElseThrow(NoSuchElementException::new);

        // FriendRequest에서 두 사용자가 requester, receiver인 friendRequest 반환
        FriendRequest foundFriendRequest = friendRepository.findInteractiveRequest(loginedUser, friend).orElseThrow(NoSuchElementException::new);

        // Status가 Accept 상태인 경우 DELETED로 변경
        if (foundFriendRequest.getStatus() == ACCEPT) {
            foundFriendRequest.setStatus(DELETED);
        }
    }

    /*
    친구 요청 API
    1. 해당 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2-1. friendRequest 테이블에 두 사용자의 데이터가 삽입된 적이 없으면 insert
    2-2. friendRequest 테이블에 두 사용자의 Status가 DELETE or REJECT인 경우 PENDING으로 변경
     */
    @Transactional
    public void requestFriend(Long loginedUserId, Long userId) {
        // 전달받은 id값을 가지는 사용자가 있는지 확인 (UserRepository 의존)
        User requester = userRepository.findById(loginedUserId).orElseThrow(NoSuchElementException::new);
        User receiver = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        // insert
        if (friendRepository.findInteractiveRequest(requester, receiver).isEmpty()) {
            friendRepository.save(new FriendRequest(requester, receiver));
        } // Status 변경
        else if (friendRepository.findInteractiveRequest(receiver, requester).get().getStatus() == REJECT
                || friendRepository.findInteractiveRequest(receiver, requester).get().getStatus() == DELETED) {
            friendRepository.findInteractiveRequest(receiver, requester).get().setStatus(PENDING);
        }
    }

    /*
    친구 요청 수락 API
    1. 해당 id값을 가지는 사용자 조회(사용자 존재 여부 확인)
    2. FriendRequest 테이블에서 두 사용자의 데이터 조회
    3. Status 값 변경
     */
    @Transactional
    public void acceptRequest(Long loginedUserId, Long userId) {
        // 전달받은 id값을 가지는 사용자가 있는지 확인 (UserRepository 의존)
        User receiver = userRepository.findById(loginedUserId).orElseThrow(NoSuchElementException::new);
        User requester = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        // FriendRequest에서 사용자가 requester, receiver인 friendRequest 반환
        FriendRequest foundFriendRequest = friendRepository.findByReceiverAndRequester(receiver, requester).orElseThrow(NoSuchElementException::new);

        // Status가 Pending 상태인 경우 ACCEPT로 변경
        if (foundFriendRequest.getStatus() == PENDING) {
            foundFriendRequest.setStatus(ACCEPT);
        }
    }


}
