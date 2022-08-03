package com.example.hello.Dto.Response.User;

import com.example.hello.Entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Schema(description = "회원의 개인정보가 담긴 dto")
@Data
public class UserDto {

    private String userEmail;

    private String userName;

    private String userPhone;

    private String user_addr1;

    private String user_addr2;

    private String user_addr3;

    private LocalDateTime regiDate;


    public static UserDto from(UserEntity userEntity, ModelMapper modelMapper){

        UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        return userDto;
    }

}
