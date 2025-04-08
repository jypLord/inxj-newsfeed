package inxj.newsfeed.friend.service;

import inxj.newsfeed.friend.dto.FriendResponseDto;
import inxj.newsfeed.friend.repository.FriendRepository;
import inxj.newsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static inxj.newsfeed.friend.entity.Status.ACCEPT;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    /*
    친구 목록 조회 API
    FriendRequest에서 Status가 Accept인 조회
     */
    public List<FriendResponseDto> findAllFriends(Long userId){
        // 전달받은 id를 가지는 User 가져오기 (UserRepository 의존)
        // Todo: user가 null인 경우 예외처리
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        // user가 receiver or requester에 있는 튜플 중 status Accept인 데이터 조회
        List<User> foundFriends = friendRepository.findByRequesterOrReceiverAndStatus(user, user, ACCEPT);

        List<FriendResponseDto> responseDtos = new ArrayList<>();

        // foundFriends를 ResponseDtos에 삽입
        for(User friend : foundFriends){
            responseDtos.add(new FriendResponseDto(friend));
        }

        return responseDtos;
    }
}
