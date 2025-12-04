package com.vishnu.finance.finance_tracker.service;

import com.vishnu.finance.finance_tracker.dto.BudgetDTO;
import com.vishnu.finance.finance_tracker.entity.Budget;
import com.vishnu.finance.finance_tracker.entity.Category;
import com.vishnu.finance.finance_tracker.entity.TransactionType;
import com.vishnu.finance.finance_tracker.entity.User;
import com.vishnu.finance.finance_tracker.repository.BudgetRepository;
import com.vishnu.finance.finance_tracker.repository.CategoryRepository;
import com.vishnu.finance.finance_tracker.repository.TransactionRepository;
import com.vishnu.finance.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public BudgetDTO createBudget(Long userId, BudgetDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Category category = categoryRepository.findByIdAndUserId(dto.getCategoryId(), userId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category);
        budget.setBudgetAmount(dto.getBudgetAmount());
        budget.setMonth(dto.getMonth() != null ? dto.getMonth() : YearMonth.now());

        Budget saved = budgetRepository.save(budget);
        return convertToDTO(saved);
    }

    public List<BudgetDTO> getUserBudgets(Long userId) {
        return budgetRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BudgetDTO> getBudgetsByMonth(Long userId, YearMonth month) {
        return budgetRepository.findByUserIdAndMonth(userId, month)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BudgetDTO updateBudget(Long userId, Long budgetId, BudgetDTO dto) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        
        if (!budget.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (dto.getBudgetAmount() != null) {
            budget.setBudgetAmount(dto.getBudgetAmount());
        }

        Budget updated = budgetRepository.save(budget);
        return convertToDTO(updated);
    }

    public void deleteBudget(Long userId, Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        
        if (!budget.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        budgetRepository.deleteById(budgetId);
    }

    private BudgetDTO convertToDTO(Budget budget) {
        BigDecimal spent = transactionRepository.sumByUserCategoryAndMonth(
                budget.getUser().getId(),
                budget.getCategory().getId(),
                TransactionType.EXPENSE,
                budget.getMonth().getMonthValue(),
                budget.getMonth().getYear()
        );
        spent = spent != null ? spent : BigDecimal.ZERO;
        BigDecimal remaining = budget.getBudgetAmount().subtract(spent);

        return new BudgetDTO(
                budget.getId(),
                budget.getUser().getId(),
                budget.getCategory().getId(),
                budget.getCategory().getName(),
                budget.getBudgetAmount(),
                budget.getMonth(),
                spent,
                remaining
        );
    }
}
