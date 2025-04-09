package inxj.newsfeed.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    INVALID_EMAIL("유효하지 않은 이메일입니다.", HttpStatus.BAD_REQUEST, 400),
    INVALID_PASSWORD("유효하지 않은 비밀번호입니다.", HttpStatus.BAD_REQUEST, 400),
    INVALID_USER_ID("유효하지 않은 사용자 ID입니다.", HttpStatus.BAD_REQUEST, 400),
    INVALID_POST_ID("유효하지 않은 게시글 ID입니다.", HttpStatus.BAD_REQUEST, 400),
    //401 UNAUTHORIZED
    UNAUTHORIZED_USER_ID("유효하지 않은 게시글 ID입니다.", HttpStatus.UNAUTHORIZED, 401),

    // 404 Not Found
    NOT_FOUND_EMAIL("이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 404),
    NOT_FOUND_PASSWORD("비밀번호를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 404),
    NOT_FOUND_USER_ID("사용자 ID를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 404),
    NOT_FOUND_POST_ID("게시글 ID를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 404),
    NOT_FOUND_COMMENT_ID("해당하는 댓글이 없습니다",HttpStatus.NOT_FOUND,404),
    NOT_FOUND_COMMENTLIKE_ID("해당하는 항목이 없습니다.",HttpStatus.NOT_FOUND,404),

    // 409 Conflict
    CONFLICT_EMAIL("중복된 이메일입니다.", HttpStatus.CONFLICT, 409),
    CONFLICT_PASSWORD("중복된 비밀번호입니다.", HttpStatus.CONFLICT, 409);

    private final String message;
    private final HttpStatus httpStatus;
    //변경가능
    private final int Code;
}

