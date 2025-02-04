package com.cbm.billing.dto.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawAccountDTO {

    @Min(value = 0, message = "Amount should be greater than 0")
    @NotNull(message = "Amount should not be null")
    private Double amount;
}
