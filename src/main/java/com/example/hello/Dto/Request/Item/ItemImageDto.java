package com.example.hello.Dto.Request.Item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

//아이템 이미지 업로드용 dto
@Data
@Schema(description = "아이템 이미지 업로드시 사용하는 dto")
public class ItemImageDto {

    private int itemImageId;

    //이미지 파일
    @Schema(description = "이미지 파일 MultipartFile 객체", nullable = false)
    @NotNull(message = "이미지 파일이 없습니다.")
    MultipartFile file;
}
