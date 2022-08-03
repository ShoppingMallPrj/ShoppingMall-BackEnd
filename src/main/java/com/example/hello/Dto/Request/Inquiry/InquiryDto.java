package com.example.hello.Dto.Request.Inquiry;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Schema(description = "질문사항 입력 받을 시 Dto")
@Data
public class InquiryDto {

    //문의 제목
    @Schema(description = "문의 제목",nullable = false)
    @Size(min = 5, max = 50, message = "문의사항 제목은 5~50자 제한입니다!")
    private String inquiryTitle;

    //문의 내용
    @Schema(description = "문의 내용",nullable = false)
    @Size(min = 5, max = 100, message = "문의사항은 내용은 5~50자 제한입니다!")
    private String inquiryContent;

//    //문의 비밀 여우
//    @Schema(description = "문의 비밀 여부",nullable = false)
//    private boolean isSecret;
//
//    //문의글 비밀번호
//    @Schema(description = "문의 비밀번호",nullable = false)
//    private String inquiryPw;


}
