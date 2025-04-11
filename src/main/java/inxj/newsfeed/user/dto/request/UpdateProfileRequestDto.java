package inxj.newsfeed.user.dto.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class UpdateProfileRequestDto {

    @Size(min=8)
    private final String password;

    @Past(message = "생일은 과거 날짜여야 합니다.")
    private final LocalDate birthday;

    private final String gender;

    private final String phoneNumber;

    private final String profileImageUrl;

}
