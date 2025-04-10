package inxj.newsfeed.user;


import inxj.newsfeed.user.dto.*;
import inxj.newsfeed.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping(value ="/users/{id}")
    public ResponseEntity<Void> modifyProfile(@PathVariable Long id, @RequestBody @Valid ModifyProfileRequest dto) {
        userService.modifyProfile(id, dto);  
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value ="/users")
    public ResponseEntity<List<UserSearchResponse>> searchUsers(@RequestParam String username){
        
        List<UserSearchResponse> response = userService.searchUsers(username);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping(value = "/users/{id}/password")
    public ResponseEntity<ChangePasswordResponse> iLostMyPassword(@PathVariable Long id){
        

        return new ResponseEntity<>(userService.changePassword(id), HttpStatus.OK);
    }

    @PatchMapping(value = "/users/{id}/deactive")
    public ResponseEntity<Void> deactiveUser(@PathVariable Long id ,@RequestBody DeactiveRequest dto){
        userService.deactiveUser(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
