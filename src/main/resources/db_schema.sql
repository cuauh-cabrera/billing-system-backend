CREATE DATABASE billing_system;

CREATE SCHEMA billing;

USE billing;

-- ACCOUNT TABLE --
CREATE TABLE billing_account (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    current_balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    bill_cycle_day TINYINT NOT NULL,
    last_bill_date DATE,
    status ENUM('ACTIVE', 'TERMINATED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT chk_bill_cycle_day CHECK (bill_cycle_day BETWEEN 1 AND 31),
    INDEX idx_status (status),
    INDEX idx_bill_cycle_day (bill_cycle_day)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--- INSERT Billing Account Mocked Data ---
INSERT INTO billing_account 
    (name, current_balance, bill_cycle_day, last_bill_date, status)
VALUES 
    ('John Smith', 1250.75, 15, '2024-01-15', 'ACTIVE'),
    ('Sarah Johnson', -340.50, 5, '2024-01-05', 'ACTIVE'),
    ('Tech Corp Ltd', 5780.25, 1, '2024-01-01', 'ACTIVE'),
    ('David Wilson', 0.00, 20, '2024-01-20', 'TERMINATED'),
    ('Global Services Inc', 2890.60, 10, '2024-01-10', 'ACTIVE');


---- BILL TABLE ----
CREATE TABLE bill (
    id BIGINT NOT NULL AUTO_INCREMENT,
    billing_account_id BIGINT NOT NULL,
    generation_date DATE NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    status ENUM('SETTLED', 'NOT_SETTLED') NOT NULL DEFAULT 'NOT_SETTLED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_bill_billing_account 
        FOREIGN KEY (billing_account_id) 
        REFERENCES billing_account(id),
    CONSTRAINT uq_bill_account_id UNIQUE (billing_account_id, id),
    INDEX idx_generation_date (generation_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--- INSERT Bill Mocked Data ---
INSERT INTO bill 
    (billing_account_id, generation_date, amount, status)
VALUES 
    (1, '2024-01-15', 450.25, 'SETTLED'),         
    (2, '2024-01-05', 780.90, 'NOT_SETTLED'),     
    (3, '2024-01-01', 2150.75, 'SETTLED'),        
    (3, '2024-01-01', 3629.50, 'NOT_SETTLED'),    
    (5, '2024-01-10', 1290.35, 'SETTLED');        


-- Create the second schema (summary data)
CREATE SCHEMA billing_summary;

USE billing_summary;

-- Billing account Summary Table ---
CREATE TABLE billing_account_summary (
    id BIGINT NOT NULL,  
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Billing Summary Table ---
CREATE TABLE bill_summary (
    id BIGINT NOT NULL,  -- Note: Not AUTO_INCREMENT as it's synchronized with main schema
    status ENUM('SETTLED', 'NOT_SETTLED') NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
