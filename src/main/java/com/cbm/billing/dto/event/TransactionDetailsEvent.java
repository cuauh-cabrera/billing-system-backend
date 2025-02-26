package com.cbm.billing.dto.event;

import com.cbm.billing.common.TransactionType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailsEvent {
    private Long accountId;
    private TransactionType transactionType;
    private Double previousBalance;
    private Double transactionAmount;
    private Double currentBalance;
    private LocalDate transactionDate;
}
