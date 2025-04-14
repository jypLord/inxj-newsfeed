package inxj.newsfeed.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendResponseDto {
    private String username;
    private String name;
    private String profileImageUrl;
}
