package com.cbm.billing.dto.model;

import com.cbm.billing.common.AccountStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private String name;
    private Double currentBalance;
    private AccountStatus status;
}
