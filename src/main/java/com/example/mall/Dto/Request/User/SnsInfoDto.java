package com.example.mall.Dto.Request.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnsInfoDto {
    @Schema(description = "SNS ID")
    private String id;
}
