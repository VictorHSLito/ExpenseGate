package com.victor.expensegate.config.spel;

import com.victor.expensegate.repository.ExpenseRepository;
import com.victor.expensegate.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component(value = "expenseSec")
public class ExpenseSecurity {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseSecurity(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public boolean isOwner(Long expenseId, Authentication authentication) {
        return expenseRepository.existsByIdAndOwnerUsernameEqualsIgnoreCase(expenseId, authentication.getName());
    }

    public boolean isSameDepartment(Long expenseId, Authentication authentication) {
        var user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return expenseRepository.existsByIdAndDepartment(expenseId, user.getDepartment());
    }
}
