package com.cbm.billing.model;

import com.cbm.billing.common.AccountStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {
    private Long id;
    private String name;
    private Double currentBalance;
    private Integer billCycleDay;
    private LocalDate lastBillDate;
    private AccountStatus status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
