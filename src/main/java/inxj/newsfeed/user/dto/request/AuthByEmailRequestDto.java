package inxj.newsfeed.user.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Valid
@Getter
public class AuthByEmailRequestDto {
    @NotBlank(message = "이메일은 빈칸이면 안됩니다")
    @Email(message = "이메일 형식에 맞춰 주세요")
    String email;
}
