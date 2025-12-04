package com.vishnu.finance.finance_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDTO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private BigDecimal budgetAmount;
    private YearMonth month;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
}
