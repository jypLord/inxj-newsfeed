package inxj.newsfeed.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {

    private final String profileImageUrl;
    private final String name;
    private final String username;
    private final String email;
    private final LocalDateTime birthday;
    private final String gender;

}
