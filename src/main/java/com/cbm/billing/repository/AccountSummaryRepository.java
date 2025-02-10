package com.cbm.billing.repository;

import com.cbm.billing.entity.AccountSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountSummaryRepository extends JpaRepository<AccountSummaryEntity, Long> {
}
