package com.cbm.billing.dto.update;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBillCycleDTO {

    @NotNull(message = "Bill cycle day can not be null")
    private int billCycleDay;
}
