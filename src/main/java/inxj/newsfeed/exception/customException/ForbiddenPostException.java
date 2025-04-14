package inxj.newsfeed.exception.customException;

import inxj.newsfeed.exception.ErrorCode;

public class ForbiddenPostException extends BaseException {

  public ForbiddenPostException() {
    super(ErrorCode.FORBIDDEN_POST);
  }
}
