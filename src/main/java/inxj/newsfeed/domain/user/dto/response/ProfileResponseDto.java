package inxj.newsfeed.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ProfileResponseDto {

    private final String profileImageUrl;
    private final String name;
    private final String username;
    private final String email;
    private final LocalDate birthday;
    private final String gender;

}
