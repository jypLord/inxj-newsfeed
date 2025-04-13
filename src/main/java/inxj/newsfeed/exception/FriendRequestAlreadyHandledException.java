package inxj.newsfeed.exception;

import static inxj.newsfeed.exception.ErrorCode.INVALID_FRIEND_REQUEST;

public class FriendRequestAlreadyHandledException extends BaseException {
    public FriendRequestAlreadyHandledException() {
        super(INVALID_FRIEND_REQUEST);
    }
}
