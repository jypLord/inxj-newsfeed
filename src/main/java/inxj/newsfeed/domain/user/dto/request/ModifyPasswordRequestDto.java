package inxj.newsfeed.domain.user.dto.request;


import lombok.Getter;

@Getter
public class ModifyPasswordRequestDto {
    String newPassword;
    String oldPassword;

}
