package inxj.newsfeed.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordResponseDto {
    
    private final String newPassword;
    
}