package com.victor.expensegate.controller.dto;

import java.util.List;

public record UserInfoDTO(
        String username,
        List<String> authorities
) {
}
