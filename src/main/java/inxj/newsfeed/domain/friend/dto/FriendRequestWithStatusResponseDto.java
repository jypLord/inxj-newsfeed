package inxj.newsfeed.domain.friend.dto;

import inxj.newsfeed.domain.friend.entity.Status;
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
