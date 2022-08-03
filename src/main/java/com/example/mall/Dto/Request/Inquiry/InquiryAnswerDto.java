package com.example.mall.Dto.Request.Inquiry;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "질문 답변 입력 받을 시 Dto")
public class InquiryAnswerDto {

    @Schema(description = "질문의 고유 id")
    @NotNull(message = "답변을 입력해야 합니다!")
    private int inquiryId;

    @Schema(description = "질문 답변 내용")
    @NotBlank(message = "답변을 입력해야 합니다!")
    String answer;
}
