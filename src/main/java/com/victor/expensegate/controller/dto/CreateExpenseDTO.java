package com.victor.expensegate.controller.dto;

import java.math.BigDecimal;

public record CreateExpenseDTO(String title, BigDecimal amount) {
}
