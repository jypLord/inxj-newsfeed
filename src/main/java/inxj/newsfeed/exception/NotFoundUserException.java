package inxj.newsfeed.exception;

import static inxj.newsfeed.exception.ErrorCode.NOT_FOUND_USER_ID;

public class NotFoundUserException extends BaseException {
    public NotFoundUserException() {
        super(NOT_FOUND_USER_ID);
    }
}
