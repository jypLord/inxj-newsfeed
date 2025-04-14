package inxj.newsfeed.exception.customException;

import inxj.newsfeed.exception.ErrorCode;

public class NotFoundCommentIdException extends BaseException{


    public NotFoundCommentIdException()
    {
        super(ErrorCode.NOT_FOUND_COMMENT_ID);

    }
}
