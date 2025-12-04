package com.vishnu.finance.finance_tracker.service;

import com.vishnu.finance.finance_tracker.dto.DashboardDTO;
import com.vishnu.finance.finance_tracker.entity.TransactionType;
import com.vishnu.finance.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TransactionRepository transactionRepository;

    public DashboardDTO getDashboard(Long userId) {
        BigDecimal totalIncome = transactionRepository.sumByUserAndType(userId, TransactionType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumByUserAndType(userId, TransactionType.EXPENSE);
        
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;
        
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        // Get category-wise expenses
        Map<String, BigDecimal> categoryExpenses = transactionRepository.findByUserIdAndType(userId, TransactionType.EXPENSE)
                .stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, t -> t.getAmount(), BigDecimal::add)
                ));

        // Get monthly data for last 12 months
        Map<String, BigDecimal> monthlyData = new HashMap<>();
        for (int i = 11; i >= 0; i--) {
            YearMonth month = YearMonth.now().minusMonths(i);
            String key = month.toString();
            
            BigDecimal income = transactionRepository.findByUserIdAndType(userId, TransactionType.INCOME)
                    .stream()
                    .filter(t -> YearMonth.from(t.getDate()).equals(month))
                    .map(t -> t.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            monthlyData.put(key, income);
        }

        int transactionCount = transactionRepository.findByUserId(userId).size();

        return new DashboardDTO(totalIncome, totalExpense, netBalance, categoryExpenses, monthlyData, transactionCount);
    }
}
