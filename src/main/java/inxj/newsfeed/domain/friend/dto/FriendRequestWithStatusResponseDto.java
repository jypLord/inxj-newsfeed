package inxj.newsfeed.friend.dto;

import inxj.newsfeed.friend.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestWithStatusResponseDto {
    private String username;
    private String name;
    private String profileImageUrl;
    private Status status;
}
