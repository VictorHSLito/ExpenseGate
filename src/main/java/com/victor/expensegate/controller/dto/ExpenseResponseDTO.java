package com.victor.expensegate.controller.dto;

import com.victor.expensegate.entity.Expense;

import java.math.BigDecimal;

public record ExpenseResponseDTO(
        String title,
        BigDecimal amount,
        String department,
        String status) {
    public static ExpenseResponseDTO fromDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getTitle(),
                expense.getAmount(),
                expense.getDepartment().name(),
                expense.getStatus().name()
        );
    }
}
