package com.vishnu.finance.finance_tracker.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @Column(nullable = false)
    private BigDecimal amount;


    @Enumerated(EnumType.STRING)
    private TransactionType type; // INCOME / EXPENSE


    private String note;
    private LocalDateTime date;
}