package com.vishnu.finance.finance_tracker.repository;

import com.vishnu.finance.finance_tracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserId(Long userId);
    List<Budget> findByUserIdAndMonth(Long userId, YearMonth month);
    Optional<Budget> findByUserIdAndCategoryIdAndMonth(Long userId, Long categoryId, YearMonth month);
    List<Budget> findByUserIdAndCategoryId(Long userId, Long categoryId);
}
