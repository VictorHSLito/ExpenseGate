package com.victor.expensegate.repository;

import com.victor.expensegate.entity.Expense;
import com.victor.expensegate.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    boolean existsByIdAndOwnerUsernameEqualsIgnoreCase(Long expenseId, String username);
    boolean existsByIdAndDepartment(Long expenseId, Department department);
}
