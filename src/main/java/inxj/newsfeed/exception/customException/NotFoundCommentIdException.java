package inxj.newsfeed.exception.customException;

import inxj.newsfeed.exception.ErrorCode;

public class NotFoundCommentIdException extends BaseException{

    ErrorCode errorCode;

    public NotFoundCommentIdException(ErrorCode errorCode)
    {
        super(errorCode);
        this.errorCode=errorCode;

    }
}
