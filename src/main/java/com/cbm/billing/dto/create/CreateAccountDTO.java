package com.cbm.billing.dto.create;


import com.cbm.billing.common.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDTO {

    @NotNull(message = "Name should not be null")
    private String name;

    @NotNull(message = "Current balance should not be null")
    private Double currentBalance;

    @NotNull(message = "Bill cycle day should not be null")
    private int billCycleDay;

    @NotNull(message = "Status should not be null")
    private AccountStatus status;

}
