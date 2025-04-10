package inxj.newsfeed.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Valid
@Getter
public class RequestDto {

    @NotBlank(message="수정할까요?")
    String comment;

}
