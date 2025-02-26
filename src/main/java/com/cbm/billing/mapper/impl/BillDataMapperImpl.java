package com.cbm.billing.mapper.impl;

import com.cbm.billing.dto.create.CreateBillDTO;
import com.cbm.billing.entity.BillEntity;
import com.cbm.billing.mapper.IBillDataMapper;
import org.springframework.stereotype.Component;

@Component
public class BillDataMapperImpl implements IBillDataMapper {
    /**
     * function to map a CreateBillDTO to a BillEntity.
     *
     * @param createBillDTO the CreateBillDTO to map
     * @return the mapped BillEntity
     */
    @Override
    public BillEntity CreateBillDTOInToBillEntity(CreateBillDTO createBillDTO) {
        BillEntity billEntity = new BillEntity();
        billEntity.setAccountId(createBillDTO.getAccountId());
        billEntity.setAmount(createBillDTO.getAmount());
        return billEntity;
    }
}
