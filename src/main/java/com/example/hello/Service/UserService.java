package com.example.hello.Service;

import com.example.hello.Dto.Request.User.SignUpDto;
import com.example.hello.Dto.Response.User.LoginDto;
import com.example.hello.Dto.Response.User.UserDto;
import com.example.hello.Dto.Request.User.UserUpdateDto;
import com.example.hello.Entity.UserEntity;
import com.example.hello.Exception.BadRequestException;
import com.example.hello.Exception.ErrorCode;
import com.example.hello.Exception.NoAuthException;
import com.example.hello.Repository.UserRepository;
import com.example.hello.Token.TokenProvider;
import com.example.hello.Util.ModelMapperBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hello.Exception.ErrorCode.ALREADY_EMAIL_ERROR;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MailService mailService;

    @Autowired
    ModelMapperBean modelMapperBean;

    @Autowired
    SignUpCodeService signUpCodeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    //일반 계정의 로그인 시 사용
    public LoginDto login(com.example.hello.Dto.Request.User.LoginDto loginDto) {

        UserEntity userEntity = userRepository.findByUserEmail(loginDto.getEmail());

        if (userEntity == null) {
            throw new NoAuthException(ErrorCode.LOGIN_FAIL_ERROR);
        }

        if (passwordEncoder.matches(passwordEncoder.encode(loginDto.getPassword()), userEntity.getUserPw())){
            throw new NoAuthException(ErrorCode.LOGIN_FAIL_ERROR);
        }

        LoginDto loginResultDto = LoginDto.from(userEntity, tokenProvider);

        return loginResultDto;
    }

    @Transactional
    public UserEntity createUser(SignUpDto signUpDto) {

        //인증코드가 잘못되면 에러 발생시킨다.
//        if(!signUpCodeService.isValidCode(signUpDto.getUserEmail(), signUpDto.getCode())){
//            throw new NoAuthException(WRONG_SIGNUP_CODE);
//        }

        //이미 존재하는 이메일인지 확인
        boolean exist = userRepository.existsByUserEmail(signUpDto.getUserEmail());

        if (exist) {
            throw new BadRequestException(ALREADY_EMAIL_ERROR);
        } else {

            UserEntity userEntity = UserEntity.from(signUpDto, passwordEncoder);
            userRepository.save(userEntity);
            return userEntity;
        }
    }

    @Transactional
    public UserDto readUser(int userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        return UserDto.from(userEntity, modelMapperBean.modelMapper());
    }

    //유저를 수정한다. 비밀번호, 주소, 휴대전화 번호만 수정이 가능하다.
    @Transactional
    public void updateUser(int userId, UserUpdateDto userUpdateDto) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        userEntity.setUserPhone(userUpdateDto.getUserPhone());
        userEntity.setUser_addr1(userUpdateDto.getUserAddr1());
        userEntity.setUser_addr2(userUpdateDto.getUserAddr2());
        userEntity.setUser_addr3(userUpdateDto.getUserAddr3());
    }

    @Transactional
    public void deleteUser(int userId) {

        userRepository.deleteByUserId(userId);
    }
}
