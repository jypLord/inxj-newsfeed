package inxj.newsfeed.user.service;

import inxj.newsfeed.user.dto.request.ModifyProfileRequest;
import inxj.newsfeed.user.dto.request.SignUpRequest;
import inxj.newsfeed.user.dto.response.ProfileResponse;
import inxj.newsfeed.user.dto.response.SignUpResponse;

import inxj.newsfeed.user.entity.User;
import inxj.newsfeed.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public void signUp(SignUpRequest dto){
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.CONFLICT_EMAIL);
        }     

        //TODO: CONFLICT_USERNAME 추가 요청후 변경
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new CustomException(ErrorCode.CONFLICT_STATUS);  
        }    
        
        
        //비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(dto.getPassword());


        User newbie = new User(dto.getEmail(), encodedPassword,
                dto.getUsername(), dto.getName(), dto.getBirthday(),
                dto.getGender(), dto.getPhoneNumber(), dto.getProfileImageUrl());

        userRepository.save(newbie);
    }

    public void login(LoginRequest dto, HttpSession session){
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new CustomException(INVALID_EMAIL));
        
        if ( ! isPasswordValid(dto.getPassword(), user.getPassword() )){
            throw new CustomException(INVALID_PASSWORD);
        }
        session.setAttribute("loginUser", user);
    }

    public ProfileResponse viewProfile(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        return new ProfileResponse(user.getProfileImageUrl(), user.getName(),user.getUsername() ,user.getEmail(), user.getBirthday(), user.getGender());
    }
    
    @Transactional
    public void modifyProfile(Long id, ModifyProfileRequest dto){
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        if ( ! isPasswordValid(dto.getPassword(), user.getPassword() )){
            throw new CustomException(INVALID_PASSWORD);
        }
        

        user.setBirthday(dto.getBirthday());
        user.setGender(dto.getGender());
        
        // 전화번호 유일성 확인
        if (dto.getPhoneNumber() != null) {
            Optional<User> existNumber = userRepository.findByPhoneNumber(dto.getPhoneNumber());
    
            if (existNumber.isPresent()) {
                throw new CustomException(ErrorCode.CONFLICT_STATUS); 
            }
    
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        user.setProfileImageUrl(dto.getProfileImageUrl());
    } 

    public boolean isPasswordValid(String rawPassword , String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
