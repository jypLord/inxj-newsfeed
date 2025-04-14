package inxj.newsfeed.domain.user.service;

import static inxj.newsfeed.exception.ErrorCode.*;

import inxj.newsfeed.common.util.EntityFetcher;
import inxj.newsfeed.domain.user.dto.request.*;
import inxj.newsfeed.domain.user.dto.response.ProfileResponseDto;
import inxj.newsfeed.domain.user.dto.response.SearchUsersResponseDto;
import inxj.newsfeed.domain.user.entity.User;
import inxj.newsfeed.domain.user.repository.UserRepository;
import inxj.newsfeed.exception.ErrorCode;
import inxj.newsfeed.exception.customException.BaseException;
import inxj.newsfeed.exception.customException.EmailConflictException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final EntityFetcher entityFetcher;

    public void signUp(SignUpRequestDto dto) {
        String verifiedKey = dto.getEmail() + ":verified";
        String verified = redisTemplate.opsForValue().get(verifiedKey);

        if (!"true".equals(verified)) {
            throw new BaseException(UNAUTHORIZED_CODE);  // 이메일 인증 안됨 예외
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailConflictException();
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BaseException(ErrorCode.CONFLICT_STATUS);
        }



        //비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User newUser = User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .username(dto.getUsername())
                .name(dto.getName())
                .birthday(dto.getBirthday())
                .gender(dto.getGender())
                .phoneNumber(dto.getPhoneNumber())
                .profileImageUrl(dto.getProfileImageUrl())
                .build();

        userRepository.save(newUser);
    }

    public void login(LoginRequestDto dto, HttpSession session) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BaseException(INVALID_EMAIL));

        if (isPasswordValid(dto.getPassword(), user.getPassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }
        if (user.getDeletedAt()==null) {
            session.setAttribute("loginUser", user.getId());
        } else {
            throw new BaseException(INVALID_USER_ID);
        }
    }

    public ProfileResponseDto viewProfile(Long id) {
        User user = userRepository.findByIdAndDeletedAt(id, null).orElseThrow(()
                -> new BaseException(INVALID_USER_ID));

        return ProfileResponseDto.builder()
                .profileImageUrl(user.getProfileImageUrl())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail()).birthday(user.getBirthday()).gender(user.getGender()).build();
    }

    @Transactional
    public void modifyProfile(Long id, UpdateProfileRequestDto dto) {
        User user = entityFetcher.getUserOrThrow(id);

        if (isPasswordValid(dto.getPassword(), user.getPassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }

        user.setBirthday(dto.getBirthday());
        user.setGender(dto.getGender());

        // 전화번호 유일성 확인
        if (dto.getPhoneNumber() != null) {


            if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
                throw new BaseException(CONFLICT_STATUS);
            }

            user.setPhoneNumber(dto.getPhoneNumber());
        }

        user.setProfileImageUrl(dto.getProfileImageUrl());
    }


    @Transactional
    public void modifyPassword(Long id, ModifyPasswordRequestDto requestDto){
                    
        User user = entityFetcher.getUserOrThrow(id);

        if (isPasswordValid(requestDto.getOldPassword(), user.getPassword())){
            throw new BaseException(INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

    }

    public List<SearchUsersResponseDto> searchUsers(String username) {

        return userRepository.findByUsernameAndDeletedAt(username, null);
    }

    @Transactional
    public void deactivateUser(Long id, DeactivateRequestDto dto) {
        User user = entityFetcher.getUserOrThrow(id);

        if (isPasswordValid(dto.getPassword(), user.getPassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }

        user.setDeletedAt(LocalDateTime.now());
    }

    public boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return !passwordEncoder.matches(rawPassword, encodedPassword);
    }

}