package com.cbm.billing.repository;

import com.cbm.billing.entity.BillSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillSummaryRepository extends JpaRepository<BillSummaryEntity, Long> {
}
