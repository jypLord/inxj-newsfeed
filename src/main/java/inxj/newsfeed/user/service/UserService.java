package inxj.newsfeed.user.service;

import static inxj.newsfeed.exception.ErrorCode.*;

import inxj.newsfeed.common.util.EntityFetcher;
import inxj.newsfeed.exception.CustomException;
import inxj.newsfeed.user.dto.request.*;
import inxj.newsfeed.user.dto.response.ChangePasswordResponseDto;
import inxj.newsfeed.user.dto.response.ProfileResponseDto;
import inxj.newsfeed.user.dto.response.SearchUsersResponseDto;
import inxj.newsfeed.user.entity.User;
import inxj.newsfeed.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
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
    private final PasswordEncoder passwordEncoder;
    private final EntityFetcher entityFetcher;

    public void signUp(SignUpRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException(CONFLICT_EMAIL);
        }

        //TODO: CONFLICT_USERNAME 추가 요청후 변경
        if (!userRepository.findByUsername(dto.getUsername()).isEmpty()) {
            throw new CustomException(CONFLICT_STATUS);
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
                .orElseThrow(() -> new CustomException(INVALID_EMAIL));

        if (!isPasswordValid(dto.getPassword(), user.getPassword())) {
            throw new CustomException(INVALID_PASSWORD);
        }
        if (user.getDeletedAt()==null) {
            session.setAttribute("loginUser", user.getId());
        } else {
            throw new CustomException(INVALID_USER_ID);
        }
    }

    public ProfileResponseDto viewProfile(Long id) {
        User user = userRepository.findByIdAndDeletedAt(id, null).orElseThrow(() -> new CustomException(INVALID_USER_ID));

        return ProfileResponseDto.builder()
                .profileImageUrl(user.getProfileImageUrl())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail()).birthday(user.getBirthday()).gender(user.getGender()).build();
    }

    @Transactional
    public void modifyProfile(Long id, UpdateProfileRequestDto dto) {
        User user = entityFetcher.getUserOrThrow(id);

        if (!isPasswordValid(dto.getPassword(), user.getPassword())) {
            throw new CustomException(INVALID_PASSWORD);
        }

        user.setBirthday(dto.getBirthday());
        user.setGender(dto.getGender());

        // 전화번호 유일성 확인
        if (dto.getPhoneNumber() != null) {
            Optional<User> existNumber = userRepository.findByPhoneNumber(dto.getPhoneNumber());

            if (existNumber.isPresent()) {
                throw new CustomException(CONFLICT_STATUS);
            }

            user.setPhoneNumber(dto.getPhoneNumber());
        }

        user.setProfileImageUrl(dto.getProfileImageUrl());
    }


    @Transactional
    public void modifyPassword(Long id, ModifyPasswordRequestDto requestDto){
                    
        User user = entityFetcher.getUserOrThrow(id);

        if (!isPasswordValid(requestDto.getOldPassword(),user.getPassword())){
            throw new CustomException(INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

    }

    public List<SearchUsersResponseDto> searchUsers(String username) {
        List<User> users = userRepository.findByUsernameAndDeletedAt(username, null);

        List<SearchUsersResponseDto> responseDTOList = new ArrayList<>();

        for (User user : users) {
            SearchUsersResponseDto responseDto = new SearchUsersResponseDto(user.getUsername(), user.getProfileImageUrl());
            responseDTOList.add(responseDto);
        }

        return responseDTOList;
    }

    public ChangePasswordResponseDto changePassword(Long id) {
        entityFetcher.getUserOrThrow(id);

        String password="12345678910";

        return new ChangePasswordResponseDto(password);
    }

    @Transactional
    public void deactivateUser(Long id, DeactivateRequestDto dto) {
        User user = entityFetcher.getUserOrThrow(id);

        if (!isPasswordValid(dto.getPassword(), user.getPassword())) {
            throw new CustomException(INVALID_PASSWORD);
        }

        user.setDeletedAt(LocalDateTime.now());
    }

    public boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}