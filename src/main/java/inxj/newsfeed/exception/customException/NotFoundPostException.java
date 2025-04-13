package inxj.newsfeed.exception.customException;

import inxj.newsfeed.exception.ErrorCode;

public class NotFoundPostException extends BaseException {

  public NotFoundPostException() {
    super(ErrorCode.NOT_FOUND_POST_ID);
  }
}
