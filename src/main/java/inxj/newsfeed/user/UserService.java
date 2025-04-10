package inxj.newsfeed.user.service;

import javax.transaction.Transactional;
import javax.servlet.http.HttpSession;

import inxj.newsfeed.user.dto.*;


import inxj.newsfeed.user.entity.User;
import inxj.newsfeed.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.ArrayList;



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
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.INVALID_EMAIL));
        
        if ( ! isPasswordValid(dto.getPassword(), user.getPassword() )){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        
        if(user.deletedAt != null){
        session.setAttribute("loginUser", user);
        }else{
            throw new CustomException(ErrorCode.INVALID_USER_ID_);
        }
    }

    public ProfileResponse viewProfile(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_ID));

        return new ProfileResponse(user.getProfileImageUrl(), user.getName(),user.getUsername() ,user.getEmail(), user.getBirthday(), user.getGender());
    }
    
    @Transactional
    public void modifyProfile(Long id, ModifyProfileRequest dto){
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_ID));

        if ( ! isPasswordValid(dto.getPassword(), user.getPassword() )){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
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
    public List<SearchUsersResponse> searchUsers(String username) {
        List<User> users = userRepository.findByUsername(username);
        
        List<SearchUsersResponse> responseDTOList = new ArrayList<>();

        for (User user : users) {
            SearchUsersResponse reponseDTO = new SearchUsersResponse(user.getUsername(), user.getProfileImageUrl());
            responseDTOList.add(reponseDTO);
        }

        return responseDTOList;
    }
    public ChangePasswordResponse changePassword(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_ID));
        
        return new ChangePasswordResponse(user.getPassword());
    }

    @Transactional
    public void deactiveUser(Long id, DeactiveUser dto){
       User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_ID));

       if( ! isPasswordValid(dto.getPassword(), user.getPassword()) ){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
       } 
       
       user.setDeletedAt(LocalDateTime.now());
       userRepository.save(user);
    }


    public boolean isPasswordValid(String rawPassword , String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


}
