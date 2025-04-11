package inxj.newsfeed.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_EMAIL("유효하지 않은 이메일입니다.", HttpStatus.BAD_REQUEST, "400-001"),
    INVALID_PASSWORD("유효하지 않은 비밀번호입니다.", HttpStatus.BAD_REQUEST, "400-002"),
    INVALID_USER_ID("유효하지 않은 사용자 ID입니다.", HttpStatus.BAD_REQUEST, "400-003"),
    INVALID_POST_ID("유효하지 않은 게시글 ID입니다.", HttpStatus.BAD_REQUEST, "400-004"),
    INVALID_FRIEND_REQUEST("유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST, "400-005"),
    INVALID_CODE("유효하지 않은 인증번호입니다.", HttpStatus.UNAUTHORIZED, "400-006"),


    UNAUTHORIZED_USER_ID("인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED, "401-001"),
    UNAUTHORIZED_CODE("인증되지 않은 인증번호입니다.", HttpStatus.UNAUTHORIZED, "401-002"),

    NOT_FOUND_EMAIL("이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-001"),
    NOT_FOUND_PASSWORD("비밀번호를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-002"),
    NOT_FOUND_USER_ID("사용자 ID를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-003"),
    NOT_FOUND_POST_ID("게시글 ID를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-004"),
    NOT_FOUND_COMMENT_ID("댓글 ID를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-005"),
    NOT_FOUND_LIKE_ID("좋아요 ID를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-006"),

    CONFLICT_EMAIL("중복된 이메일입니다.", HttpStatus.CONFLICT, "409-001"),
    CONFLICT_PASSWORD("중복된 비밀번호입니다.", HttpStatus.CONFLICT, "409-002"),
    CONFLICT_STATUS("중복된 요청입니다.", HttpStatus.CONFLICT, "409-003");

    private final String message;
    private final HttpStatus httpStatus;
    //이름이 뭔가 ....
    private final String errorCode;
}


