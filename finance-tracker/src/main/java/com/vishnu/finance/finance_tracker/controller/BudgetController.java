package com.vishnu.finance.finance_tracker.controller;

import com.vishnu.finance.finance_tracker.dto.BudgetDTO;
import com.vishnu.finance.finance_tracker.service.BudgetService;
import com.vishnu.finance.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getBudgets(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        return ResponseEntity.ok(budgetService.getUserBudgets(userId));
    }

    @GetMapping("/month/{month}")
    public ResponseEntity<List<BudgetDTO>> getBudgetsByMonth(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String month) {
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        YearMonth yearMonth = YearMonth.parse(month);
        return ResponseEntity.ok(budgetService.getBudgetsByMonth(userId, yearMonth));
    }

    @PostMapping
    public ResponseEntity<BudgetDTO> createBudget(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BudgetDTO dto) {
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        BudgetDTO created = budgetService.createBudget(userId, dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody BudgetDTO dto) {
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        return ResponseEntity.ok(budgetService.updateBudget(userId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        budgetService.deleteBudget(userId, id);
        return ResponseEntity.noContent().build();
    }
}
