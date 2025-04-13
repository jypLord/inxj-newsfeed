package inxj.newsfeed.domain.user.controller;

import inxj.newsfeed.domain.user.dto.request.*;
import inxj.newsfeed.domain.user.dto.response.ChangePasswordResponseDto;
import inxj.newsfeed.domain.user.dto.response.ProfileResponseDto;
import inxj.newsfeed.domain.user.dto.response.SearchUsersResponseDto;
import inxj.newsfeed.domain.user.service.UserService;
import inxj.newsfeed.exception.ErrorCode;
import inxj.newsfeed.exception.customException.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static inxj.newsfeed.common.constant.Const.LOGIN_USER;

@RestController
@RequiredArgsConstructor
@Validated
//@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/users")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDto dto) {
        userService.signUp(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<ProfileResponseDto> viewProfile(@PathVariable Long id,
                                                          @SessionAttribute(LOGIN_USER) Long loginUserId) {

        if (!Objects.equals(id, loginUserId)) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_USER_ID);
        }

        return ResponseEntity.ok(userService.viewProfile(id));
    }

    @PatchMapping(value = "/users/{id}")
    public ResponseEntity<Void> modifyProfile(@PathVariable Long id,
                                              @RequestBody @Valid UpdateProfileRequestDto dto,
                                              @SessionAttribute(LOGIN_USER) Long loginUserId) {

        if (!Objects.equals(id, loginUserId)) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_USER_ID);
        }


        userService.modifyProfile(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<SearchUsersResponseDto>> searchUsers(@RequestParam String username) {

        List<SearchUsersResponseDto> response = userService.searchUsers(username);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/users/{id}/password")
    public ResponseEntity<Void> modifyMyPassword(@PathVariable Long id,
                                                 @RequestBody ModifyPasswordRequestDto requestDto,
                                                 @SessionAttribute(LOGIN_USER) Long loginUserId) {

        if (!Objects.equals(id, loginUserId)) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_USER_ID);
        }

        userService.modifyPassword(id, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/users/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id,
                                               @RequestBody DeactivateRequestDto dto,
                                               @SessionAttribute(LOGIN_USER) Long loginUserId) {
        userService.deactivateUser(id, dto);

        if (!Objects.equals(id, loginUserId)) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_USER_ID);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}