package inxj.newsfeed.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeactivateRequestDto {

    private final String password;

}