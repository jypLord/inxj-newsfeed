package inxj.newsfeed.user.controller;

import inxj.newsfeed.user.dto.request.AuthByEmailRequestDto;
import inxj.newsfeed.user.dto.request.CheckingByEmailRequestDto;
import inxj.newsfeed.user.dto.request.LoginRequestDto;
import inxj.newsfeed.user.service.AuthByEmailService;
import inxj.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final UserService userService;
    private final AuthByEmailService authByEmailService;

    @PostMapping( "/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto dto, HttpServletRequest request) {

        HttpSession session =request.getSession();

        userService.login(dto,session);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        HttpSession session=request.getSession(false);

        if(session!=null){
            session.invalidate();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/auth")
    public  ResponseEntity<Void> sendMail(@RequestBody AuthByEmailRequestDto requestDto){

        authByEmailService.sendEmail(requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/authMail")
    public  ResponseEntity<Void> sendMailAuth(@RequestBody CheckingByEmailRequestDto requestDto){

        authByEmailService.CheckEmailAuth(requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/password")
    public  ResponseEntity<Void> creatPassword(@RequestBody CheckingByEmailRequestDto requestDto){

        authByEmailService.findPassword(requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/email")
    public ResponseEntity<String> existEmail(@RequestBody AuthByEmailRequestDto requestDto){

            return new ResponseEntity<>(authByEmailService.existEmail(requestDto),HttpStatus.OK);
    }

}
