--liquibase formatted sql
--changeset dev:1

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================
-- TABELA: clinics
-- ============================
CREATE TABLE clinics (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================
-- TABELA: users
-- ============================
CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    keycloak_id UUID NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL, -- DOCTOR / ASSISTANT / ADMIN
    doctor_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_doctor FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ============================
-- TABELA: clinic_owners (legătură user - clinică)
-- ============================
CREATE TABLE clinic_owners (
    id UUID PRIMARY KEY,
    clinic_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_owner_clinic FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE,
    CONSTRAINT fk_owner_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_owner_per_clinic UNIQUE (clinic_id, user_id)
);

-- ============================
-- TABELA: products
-- ============================
CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    unit VARCHAR(50),
    quantity INTEGER NOT NULL DEFAULT 0,
    low_stock_threshold INTEGER DEFAULT 0,
    expiration_date DATE,
    clinic_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_clinic FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ============================
-- TABELA: inventory_log
-- ============================
CREATE TABLE inventory_log (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    action_type VARCHAR(10) NOT NULL,
    quantity INTEGER NOT NULL,
    reason TEXT,
    user_id UUID NOT NULL,
    clinic_id UUID NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_log_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_log_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_log_clinic FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE
);

-- ============================
-- TABELA: appointments
-- ============================
CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    date_time TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL DEFAULT 30,
    patient_name VARCHAR(90) NOT NULL,
    patient_phone VARCHAR(50) NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    clinic_id UUID NOT NULL,
    user_id UUID NOT NULL,

    CONSTRAINT fk_appointment_clinic FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE,
    CONSTRAINT fk_appointment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ============================
-- TABELA: invitations
-- ============================
CREATE TABLE invitations (
    id UUID PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    clinic_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    doctor_id UUID,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_invitation_clinic FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE,
    CONSTRAINT fk_invitation_doctor FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE SET NULL
);
