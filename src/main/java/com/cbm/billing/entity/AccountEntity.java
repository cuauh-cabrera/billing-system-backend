package com.cbm.billing.entity;

import com.cbm.billing.commons.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotNull(message = "Name should not be null")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Current balance should not be null")
    @Column(name = "current_balance")
    private Double currentBalance;

    @NotNull(message = "Bill cycle day should not be null")
    @Column(name = "bill_cycle_day")
    private int billCycleDay;

    @NotNull(message = "Last bill date should not be null")
    @Column(name = "last_bill_date")
    private LocalDate lastBillDate;

    @NotNull(message = "Status should not be null")
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
