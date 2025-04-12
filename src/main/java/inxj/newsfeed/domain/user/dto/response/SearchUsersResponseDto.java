package inxj.newsfeed.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchUsersResponseDto {

    private final String username;
    private final String profileImageUrl;

}