package com.cbm.billing.mapper;

import com.cbm.billing.dto.create.CreateBillDTO;
import com.cbm.billing.entity.BillEntity;

public interface IBillDataMapper {
    public BillEntity CreateBillDTOInToBillEntity(CreateBillDTO createBillDTO);
}
