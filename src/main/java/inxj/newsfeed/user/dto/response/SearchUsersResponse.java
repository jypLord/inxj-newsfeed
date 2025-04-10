package inxj.newsfeed.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SearchUsersResponse {
    
    private final String username;
    private final String profileImageUrl;
}    