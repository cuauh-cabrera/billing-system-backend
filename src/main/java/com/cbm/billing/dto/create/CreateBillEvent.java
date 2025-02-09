package com.cbm.billing.dto.create;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBillEvent {
    private Long accountId;
    private Double currentBalance;
    private Double billAmount;
    private Double newBalance;
    private LocalDate billGenerationDate;
}
