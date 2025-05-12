--liquibase formatted sql
--changeset dev:1

-- ============================
-- Activare extensie UUID
-- ============================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================
-- TABELA: clinics (cabinete)
-- ============================
CREATE TABLE clinics (
    id UUID PRIMARY KEY, -- ID unic pentru fiecare clinică
    name VARCHAR(255) NOT NULL, -- Numele clinicii (ex: DentSmile)
    address TEXT, -- Adresă clinică (opțional)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data înregistrării
);

-- ============================
-- TABELA: users (medici / asistenți)
-- ============================
CREATE TABLE users (
    id UUID PRIMARY KEY, -- ID intern al utilizatorului
    username VARCHAR(100) NOT NULL UNIQUE, -- Nume afișat sau folosit în UI
    keycloak_id UUID NOT NULL UNIQUE, -- ID-ul unic din Keycloak
    role VARCHAR(50) NOT NULL, -- Rol: ADMIN / DOCTOR / ASSISTANT
    clinic_id UUID NOT NULL, -- FK către clinica în care activează
    doctor_id UUID, -- Dacă userul este asistent, legat la un doctor
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_clinic FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_doctor FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ============================
-- TABELA: products (consumabile)
-- ============================
CREATE TABLE products (
    id UUID PRIMARY KEY, -- ID unic produs
    name VARCHAR(255) NOT NULL, -- Nume produs (ex: Lidocain 2%)
    category VARCHAR(100), -- Categorie (ex: Anestezice, Consumabile)
    unit VARCHAR(50), -- Unitate de măsură (bucăți, ml)
    quantity INTEGER NOT NULL DEFAULT 0, -- Cantitate în stoc
    low_stock_threshold INTEGER DEFAULT 0, -- Prag pentru alertă
    expiration_date DATE, -- Data expirării (opțional)
    clinic_id UUID NOT NULL, -- FK către clinică
    user_id UUID NOT NULL, -- FK către utilizatorul care a adăugat
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_clinic FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ============================
-- TABELA: inventory_log (istoric consum)
-- ============================
CREATE TABLE inventory_log (
    id UUID PRIMARY KEY, -- ID unic log
    product_id UUID NOT NULL, -- FK către produs
    action_type VARCHAR(10) NOT NULL, -- "IN" (intrare) / "OUT" (consum)
    quantity INTEGER NOT NULL, -- Cantitate modificată
    reason TEXT, -- Motivul acțiunii (ex: „folosit tratament carie”)
    user_id UUID NOT NULL, -- FK către utilizatorul care a făcut modificarea
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_log_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_log_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ============================
-- TABELA: appointments (programări)
-- ============================
CREATE TABLE appointments (
    id UUID PRIMARY KEY, -- ID unic programare
    date_time TIMESTAMP NOT NULL, -- Data și ora programării
    duration_minutes INTEGER NOT NULL DEFAULT 30, -- Durată programare în minute
    patient_name VARCHAR(100) NOT NULL, -- Numele pacientului
    reason TEXT, -- Motivul programării (ex: consult, durere, detartraj)
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED / COMPLETED / CANCELED
    clinic_id UUID NOT NULL, -- FK către clinică
    user_id UUID NOT NULL, -- FK către utilizator (medic)

    CONSTRAINT fk_appointment_clinic FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE,
    CONSTRAINT fk_appointment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ============================
-- TABELA: invitations (pentru înregistrare asistente/doctori)
-- ============================
CREATE TABLE invitations (
    id UUID PRIMARY KEY, -- ID unic invitație
    token VARCHAR(255) NOT NULL UNIQUE, -- Token securizat (UUID sau JWT)
    clinic_id UUID NOT NULL, -- FK către clinica asociată
    role VARCHAR(50) NOT NULL, -- ASSISTANT / DOCTOR / ADMIN
    doctor_id UUID, -- Dacă e asistentă, se leagă la doctorul respectiv
    expires_at TIMESTAMP NOT NULL, -- Dată expirare invitație
    used BOOLEAN DEFAULT FALSE, -- Dacă a fost folosită deja
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_invitation_clinic FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE,
    CONSTRAINT fk_invitation_doctor FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE SET NULL
);
