package com.cbm.billing.dto.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAmountDTO {

    @Min(value = 0, message = "Amount should be greater than 0")
    @NotNull(message = "Amount should not be null")
    private Double amount;
}
