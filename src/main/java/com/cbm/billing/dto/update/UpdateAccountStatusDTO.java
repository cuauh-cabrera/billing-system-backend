package com.cbm.billing.dto.update;

import com.cbm.billing.common.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountStatusDTO {

    @NotNull(message = "Status should not be null")
    private AccountStatus status;
}
