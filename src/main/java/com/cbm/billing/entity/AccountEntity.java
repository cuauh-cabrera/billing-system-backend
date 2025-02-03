package com.cbm.billing.entity;

import com.cbm.billing.common.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(catalog = "billing", name = "billing_account")
public class AccountEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "current_balance")
    private Double currentBalance;

    @Column(name = "bill_cycle_day")
    private int billCycleDay;

    @Column(name = "last_bill_date")
    private LocalDate lastBillDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY)
    private List<BillEntity> bills;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}
