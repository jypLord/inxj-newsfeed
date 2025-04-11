package inxj.newsfeed.friend.dto;

import inxj.newsfeed.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendResponseDto {
    private String username;
    private String name;
    private String profileImageUrl;
}
