package com.cbm.billing.service;

import com.cbm.billing.dto.create.CreateBillDTO;
import com.cbm.billing.dto.create.CreateBillResponse;
import com.cbm.billing.exception.AccountNotFoundException;

public interface IBillService {
    public CreateBillResponse createBill(CreateBillDTO createBillDTO) throws AccountNotFoundException;
}
