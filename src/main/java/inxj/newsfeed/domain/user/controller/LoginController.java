package inxj.newsfeed.domain.user.controller;

import inxj.newsfeed.domain.user.dto.request.AuthByEmailRequestDto;
import inxj.newsfeed.domain.user.dto.request.CheckingByEmailRequestDto;
import inxj.newsfeed.domain.user.dto.request.LoginRequestDto;
import inxj.newsfeed.domain.user.service.AuthByEmailService;
import inxj.newsfeed.domain.user.service.UserService;
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
    public  ResponseEntity<Void> sendMail(@RequestBody AuthByEmailRequestDto requestDto,
                                          HttpServletRequest request){

        authByEmailService.sendEmail(requestDto,request);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/authMail")
    public  ResponseEntity<Void> sendMailAuth(@RequestBody CheckingByEmailRequestDto requestDto,
                                              HttpServletRequest request){

        authByEmailService.CheckEmailAuth(requestDto,request);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/password")
    public  ResponseEntity<Void> creatPassword(@RequestBody CheckingByEmailRequestDto requestDto,
                                               HttpServletRequest request){

        authByEmailService.findPassword(requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/email")
    public ResponseEntity<String> existEmail(@RequestBody AuthByEmailRequestDto requestDto,
                                             HttpServletRequest request){

            return new ResponseEntity<>(authByEmailService.existEmail(requestDto),HttpStatus.OK);
    }

}
