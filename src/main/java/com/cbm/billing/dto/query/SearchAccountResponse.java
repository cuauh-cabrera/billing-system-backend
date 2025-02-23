package com.cbm.billing.dto.query;

import com.cbm.billing.model.Account;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchAccountResponse {
    private int page;
    private int size;
    private long total;
    private List<Account> accounts;
}
