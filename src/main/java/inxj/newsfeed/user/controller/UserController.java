package inxj.newsfeed.user.controller;

import inxj.newsfeed.user.dto.request.SignUpRequest;
import inxj.newsfeed.user.dto.response.ProfileResponse;
import inxj.newsfeed.user.dto.response.SignUpResponse;
import inxj.newsfeed.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/users")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest dto){
        userService.signUp(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest dto, HttpSession session){
        userService.login(dto,session);
        return new ResponseEntity<>(HttpStatus.OK); 
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<ProfileResponse> viewProfile(@PathVariable Long id){
        return ResponseEntity.ok(userService.viewProfile(id));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Void> modifyProfile(@PathVariable Long id, @RequestBody @Valid ModifyProfileRequest dto) {
        userService.modifyProfile(id, dto);  
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
