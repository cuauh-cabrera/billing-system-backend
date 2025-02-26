package com.cbm.billing.dto.query;

import com.cbm.billing.model.Account;
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
    private List <Account> accounts;
}
