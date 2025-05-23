package inxj.newsfeed.exception.customException;

import inxj.newsfeed.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}