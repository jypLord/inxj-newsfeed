package inxj.newsfeed.friend.dto;

import inxj.newsfeed.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendResponseDto {
    private User friend;
}
