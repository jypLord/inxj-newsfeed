package inxj.newsfeed.user.controller;

import inxj.newsfeed.user.dto.request.DeactivateRequestDto;
import inxj.newsfeed.user.dto.request.LoginRequestDto;
import inxj.newsfeed.user.dto.request.SignUpRequestDto;
import inxj.newsfeed.user.dto.request.UpdateProfileRequestDto;
import inxj.newsfeed.user.dto.response.ChangePasswordResponseDto;
import inxj.newsfeed.user.dto.response.ProfileResponseDto;
import inxj.newsfeed.user.dto.response.SearchUsersResponseDto;
import inxj.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/users")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDto dto) {
        userService.signUp(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto dto, HttpSession session) {
        userService.login(dto, session);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<ProfileResponseDto> viewProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.viewProfile(id));
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<Void> modifyProfile(@PathVariable Long id, @RequestBody @Valid UpdateProfileRequestDto dto) {
        userService.modifyProfile(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<SearchUsersResponseDto>> searchUsers(@RequestParam String username) {

        List<SearchUsersResponseDto> response = userService.searchUsers(username);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/users/{id}/password")
    public ResponseEntity<ChangePasswordResponseDto> iLostMyPassword(@PathVariable Long id) {
        return new ResponseEntity<>(userService.changePassword(id), HttpStatus.OK);
    }

    @PatchMapping(value = "/users/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id, @RequestBody DeactivateRequestDto dto) {
        userService.deactivateUser(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}