package com.victor.expensegate.service;

import com.victor.expensegate.controller.dto.CreateExpenseDTO;
import com.victor.expensegate.entity.Expense;
import com.victor.expensegate.enums.ExpenseStatus;
import com.victor.expensegate.repository.ExpenseRepository;
import com.victor.expensegate.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public Expense create(CreateExpenseDTO dto, Authentication authentication) {

        String username = authentication.getName();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));


        var expense = new Expense();

        expense.setTitle(dto.title());
        expense.setAmount(dto.amount());
        expense.setOwner(user);
        expense.setDepartment(user.getDepartment());
        expense.setStatus(ExpenseStatus.SUBMITTED);

        return expenseRepository.save(expense);
    }

    public Expense read(Long expenseId) {
        return expenseRepository.findById(expenseId).orElseThrow();
    }
}
