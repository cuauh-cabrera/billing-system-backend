package com.cbm.billing.dto.update;

import com.cbm.billing.dto.event.TransactionDetailsEvent;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long code;
    private String message;
    private TransactionDetailsEvent details;
}
