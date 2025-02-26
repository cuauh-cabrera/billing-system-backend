package com.cbm.billing.dto.event;

import com.cbm.billing.common.AccountStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountStatusEvent {
    private Long accountId;
    private AccountStatus updatedStatus;
    private LocalDate updatedAt;
}
