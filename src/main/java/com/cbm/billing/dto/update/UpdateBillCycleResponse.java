package com.cbm.billing.dto.update;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBillCycleResponse {
    private Long code;
    private Long accountId;
    private String message;
}
