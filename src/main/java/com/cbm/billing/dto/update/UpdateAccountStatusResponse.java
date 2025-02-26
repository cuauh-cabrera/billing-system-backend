package com.cbm.billing.dto.update;

import com.cbm.billing.dto.event.UpdateAccountStatusEvent;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountStatusResponse {
    private Long code;
    private String message;
    private UpdateAccountStatusEvent details;
}
