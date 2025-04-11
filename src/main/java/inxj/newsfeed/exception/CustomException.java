package inxj.newsfeed.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}