package inxj.newsfeed.domain.user.service;

import inxj.newsfeed.common.config.SecurityConfig;
import inxj.newsfeed.exception.ErrorCode;
import inxj.newsfeed.domain.user.dto.request.AuthByEmailRequestDto;
import inxj.newsfeed.domain.user.dto.request.CheckingByEmailRequestDto;
import inxj.newsfeed.domain.user.entity.User;
import inxj.newsfeed.domain.user.repository.UserRepository;
import inxj.newsfeed.exception.customException.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static inxj.newsfeed.exception.ErrorCode.INVALID_CODE;

@Service
@RequiredArgsConstructor
public class AuthByEmailService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String,String> redisTemplate;
    private final SecurityConfig securityConfig;

    public void sendEmail(AuthByEmailRequestDto dto, HttpServletRequest request){
        String email=dto.getEmail();

        String code = createCode();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

//        String ip=request.getRemoteAddr();

        HttpSession session=request.getSession();

        session.setAttribute("auth_status", "PENDING");

        redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);

        try{
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("InXj 이메일 인증 번호 입니다.");
            simpleMailMessage.setText("인증코드: "+code);
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }


    }

    public String existEmail(AuthByEmailRequestDto dto){
        if (userRepository.existsByEmail(dto.getEmail())){
            return "회원가입된 이메일 입니다.";
        }else {
            return "회원가입 가능한 이메일 입니다.";
        }
    }

    @Transactional
    public void findPassword(CheckingByEmailRequestDto dto){

        String email=dto.getEmail();
        String code= dto.getCode();

        String savedCode = redisTemplate.opsForValue().get(email);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String newPassword=createPassword();
        if (code.equals(savedCode)){
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("새로운 비밀번호 입니다");
            simpleMailMessage.setText("비밀번호:" + newPassword+"\n 최대한 빠르게 변경 해주세요");
            User user=userRepository.findByEmail(email).
                    orElseThrow(()->new BaseException(ErrorCode.NOT_FOUND_EMAIL));
            user.setPassword(securityConfig.passwordEncoder().encode(newPassword));
            javaMailSender.send(simpleMailMessage);

        }else{
            throw new BaseException(INVALID_CODE);
        }
    }


    public void CheckEmailAuth(CheckingByEmailRequestDto dto,HttpServletRequest request){
        String email=dto.getEmail();
        String code= dto.getCode();

        HttpSession session=request.getSession(false);

        String attribute=(String) session.getAttribute("auth_status");

        if (!attribute.equals("PENDING")){
            throw new RuntimeException("해커세요?");
        }


        String savedCode = redisTemplate.opsForValue().get(email);
        System.out.println("savedCode = " + savedCode);
        System.out.println("code = " + code);

        if (code.equals(savedCode)) {
            redisTemplate.opsForValue().set(email + ":verified", "true", 10, TimeUnit.MINUTES);
            session.invalidate();
        }
        else {
            throw new BaseException(INVALID_CODE);
        }
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private String createPassword(){
        int length=10;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*";
        try {
            Random random =SecureRandom.getInstanceStrong();
            StringBuilder builder=new StringBuilder();
            for (int i=0;i<length;i++){
                builder.append(characters.charAt(random.nextInt(characters.length()+1)));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}

