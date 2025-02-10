package com.cbm.billing.dto.query;

import com.cbm.billing.dto.model.AccountDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryAccountResponse {
    private Long code;
    private String message;
    private List <AccountDTO> accounts;
}
