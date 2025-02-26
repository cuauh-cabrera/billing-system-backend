package com.cbm.billing.dto.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBillDTO {

    @NotNull(message = "Account id should not be null")
    private Long accountId;

    @Min(value = 0, message = "Amount should be greater than 0")
    @NotNull(message = "Amount should not be null")
    private Double amount;
}
