package inxj.newsfeed.exception.customException;

import inxj.newsfeed.exception.ErrorCode;

public class EmailConflictException extends BaseException {

    public EmailConflictException() {
        super(ErrorCode.CONFLICT_EMAIL);
    }
}

