package inxj.newsfeed.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class LoginRequest{
    private final String email;
    private final String password;
}