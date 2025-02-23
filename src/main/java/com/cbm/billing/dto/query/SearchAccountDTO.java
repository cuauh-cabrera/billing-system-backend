package com.cbm.billing.dto.query;

import com.cbm.billing.common.AccountStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SearchAccountDTO {
    private int page;
    private int size;
    private String sort;
    private String name;
    private Integer billCycleDay;
    private LocalDate lastBillDate;
    private AccountStatus status;
}
