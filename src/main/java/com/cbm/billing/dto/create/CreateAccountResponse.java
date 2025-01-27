package com.cbm.billing.dto.create;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponse {
    private Long code;
    private String message;
}
