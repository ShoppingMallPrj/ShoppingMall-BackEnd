package com.example.mall.Dto.Response.Inquiry;

import com.example.mall.Entity.InquiryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Schema(description = "문의사항 클라이언트 전달 시 사용하는 dto")
@Data
public class InquiryDto {

    @Schema(description = "문의 사항 고유 id",nullable = false)
    private int inquiryId;

    @Schema(description = "작성자 고유 id",nullable = false)
    private int userId;

    @Schema(description = "작성자 고유 이름",nullable = false)
    private String userEmail;

    //문의 제목
    @Schema(description = "문의 제목",nullable = false)
    @Size(min = 5, max = 50, message = "문의사항 제목은 5~50자 제한입니다!")
    private String inquiryTitle;

    @Schema(description = "답변됨 여부",nullable = false)
    @NotNull
    private boolean isAnswered;

    //답변 내용
    @Schema(description = "비밀글 여부",nullable = false)
    private boolean isSecret;

    //작성시간
    @Schema(description = "작성 시간",nullable = false)
    private LocalDateTime inquiryTime;

    //Entity에서 dto 변환
    public static InquiryDto from(InquiryEntity inquiryEntity, ModelMapper modelMapper){

        InquiryDto inquiryDto = modelMapper.map(inquiryEntity, InquiryDto.class);
        inquiryDto.setUserId(inquiryEntity.getUserEntity().getUserId());
        inquiryDto.setUserEmail(inquiryEntity.getUserEntity().getUserEmail());
        return inquiryDto;
    }

    public static Set<InquiryDto> from(Set<InquiryEntity> inquiryEntitySet, ModelMapper modelMapper) {

        Set<InquiryDto> inquiryDtos = new HashSet<>();
        Iterator<InquiryEntity> iterator = inquiryEntitySet.iterator();
        while (iterator.hasNext()) {
            inquiryDtos.add(InquiryDto.from(iterator.next(), modelMapper));
        }
        return inquiryDtos;
    }

    //Entitiy Page에서 Entity dto로 변환
    public static Page<InquiryDto> from(Page<InquiryEntity> inquiryEntityPage, ModelMapper modelMapper) {
        Page<InquiryDto> inquiryDtos = inquiryEntityPage.map(entity -> {
            InquiryDto dto = InquiryDto.from(entity, modelMapper);
            return dto;
        });
        return inquiryDtos;
    }
}
