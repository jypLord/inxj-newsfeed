package inxj.newsfeed.user.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Valid
@Getter
@AllArgsConstructor
public class SignUpRequestDto {
    @Email
    private final String email;

    @Size(min = 8)
    private final String password;

    @NotBlank
    private final String username;

    @Size(min = 2, max = 50, message = "이름은 2자 이상, 50자 이하로 입력해야 합니다.")
    private final String name;

    @Past(message = "생일은 과거 날짜여야 합니다.")
    private final LocalDateTime birthday;

    private final String gender;

    private final String phoneNumber;

    private final String profileImageUrl;

}