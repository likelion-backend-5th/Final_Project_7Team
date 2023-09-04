package com.likelion.catdogpia.domain.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class OrderStatusUpdateDto {

    private Long id;
    private String status;
}
