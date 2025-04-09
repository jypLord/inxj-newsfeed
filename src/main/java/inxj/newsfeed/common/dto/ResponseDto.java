package inxj.newsfeed.common.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseDto<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(false, message, null);
    }

}