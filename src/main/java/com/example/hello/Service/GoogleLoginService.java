package com.example.hello.Service;

import com.example.hello.Dto.Request.User.GoogleUserDto;
import com.example.hello.Dto.Response.User.LoginDto;
import com.example.hello.Entity.UserEntity;
import com.example.hello.Repository.UserRepository;
import com.example.hello.Token.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleLoginService {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public LoginDto googleLogin(String tokenId) {

        GoogleUserDto googleUserDto = getGoogleUser(tokenId);
        LoginDto loginDto = handleGoogleUserDto(googleUserDto);

        return loginDto;
    }

    public GoogleUserDto getGoogleUser(String tokenId) {
        System.out.println(tokenId);
        //가지고 온 토큰으로 google에 요청
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + tokenId;

        //구글 유저를 가져온다.
        ResponseEntity<GoogleUserDto> response
                = restTemplate.getForEntity(url, GoogleUserDto.class);

        return response.getBody();

    }

    private LoginDto handleGoogleUserDto(GoogleUserDto googleUserDto){

        String email = googleUserDto.getEmail();

        //유저가 있으면
        if(userRepository.existsByUserEmail(email)){

            UserEntity userEntity = userRepository.findByUserEmail(email);
            return LoginDto.from(userEntity, tokenProvider);

        //없으면 회원가입 처리
        } else {

            UserEntity userEntity =  UserEntity.snsFrom(email, passwordEncoder);
            userRepository.save(userEntity);

            return LoginDto.from(userEntity, tokenProvider);
        }
    }
}
