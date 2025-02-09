package com.cbm.billing.dto.create;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBillResponse {
    private Long code;
    private String message;
    private CreateBillEvent details;
}
