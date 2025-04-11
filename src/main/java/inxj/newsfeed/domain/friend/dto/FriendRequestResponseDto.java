package inxj.newsfeed.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestResponseDto {
    private String username;
    private String name;
    private String profileImageUrl;
}
