package inxj.newsfeed.domain.user.dto.request;


import lombok.Getter;

@Getter
//이름별로...
public class CheckingByEmailRequestDto {

    String email;
    String code;


}
