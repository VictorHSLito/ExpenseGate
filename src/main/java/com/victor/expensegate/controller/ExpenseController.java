package com.victor.expensegate.controller;

import com.victor.expensegate.controller.dto.CreateExpenseDTO;
import com.victor.expensegate.controller.dto.ExpenseResponseDTO;
import com.victor.expensegate.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.victor.expensegate.controller.dto.ExpenseResponseDTO.fromDTO;
import static com.victor.expensegate.entity.Authority.Values.*;

@RestController
@RequestMapping(path = "/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('" + EXP_CREATE + "')")
    public ResponseEntity<ExpenseResponseDTO> createExpense(
            @RequestBody CreateExpenseDTO dto,
            Authentication authentication
    ) {
        var expense = expenseService.create(dto, authentication);

        var expenseResponseDTO = fromDTO(expense);

        return ResponseEntity.created(URI.create("/expenses/" + expense.getId())).body(expenseResponseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + EXP_READ + "') and " +
            "hasAuthority('expense:read:dept') or " +
            "(@expenseSec.isOwner(#id, authentication)) or " +
            "(@expenseSec.isSameDepartment(#id, authentication)) or " +
            "hasAuthority('" + EXP_READ_ANY + "')")
    public ResponseEntity<ExpenseResponseDTO> read(
            @P("id") @PathVariable(name = "id") Long expenseId,
            Authentication authentication
    ) {
        var expense = expenseService.read(expenseId);
        return ResponseEntity.ok().body(fromDTO(expense));
    }
}
