# 🦷 DentaTrack – Backend

**DentaTrack** este o aplicație modernă pentru gestionarea clinicilor stomatologice, care oferă funcționalități precum evidența stocurilor, programări pacienți, log-uri de consum și managementul personalului prin roluri și invitații. Aceasta este partea de **backend**, dezvoltată cu Java, Spring Boot și Keycloak.

---

## 🧱 Tehnologii folosite

- Java 17
- Spring Boot 3
- Spring Security + Keycloak
- PostgreSQL
- Liquibase (migrări DB)
- ModelMapper
- Apache POI (export Excel)
- Docker (recomandat pentru DB/Keycloak local)
- JWT pentru autentificare

---

## 📦 Funcționalități principale

| Modul             | Descriere                                                                 |
|------------------|--------------------------------------------------------------------------|
| 👤 Autentificare | Integrare completă cu Keycloak (register/login/delete + token)           |
| 🏥 Clinici       | Un doctor poate crea și administra una sau mai multe clinici             |
| 👥 Utilizatori   | Utilizatori cu roluri (ADMIN / DOCTOR / ASSISTANT)                       |
| 📬 Invitații     | Invitații pentru alți doctori/asistente în clinică                       |
| 📦 Stocuri       | Adăugare, consum, prag minim, expirare produse                           |
| 📊 Log stocuri   | Istoric complet cu acțiuni (IN / OUT), motiv și utilizator               |
| 📅 Programări    | Sistem de programări pacienți, cu status și durată                       |
| 📤 Export        | Export Excel pentru loguri, cu filtrare după perioadă                    |
| 📈 Dashboard     | Statistici lunare despre clinică (programări, consum, alerte)            |

---

## 📌 API Reference

> Prefix comun: `/api/v1`  
> Toate endpoint-urile necesită autentificare (`Authorization: Bearer <token>`)  
> Datele sunt în format `application/json`

---

### 🔐 Autentificare

#### `POST /api/v1/auth/register`
Creează un cont nou (Keycloak + DB).

- Body:
```json
{
  "email": "user@example.com",
  "password": "secret123",
  "firstName": "Ion",
  "lastName": "Popescu",
  "role": "DOCTOR"
}
```

#### `POST /api/v1/auth/login` – Autentificare
```json
{
  "email": "user@example.com",
  "password": "parola123"
}
```

### 🏥 Clinici

#### `POST /api/v1/clinics` – Creează clinică
```json
{
  "name": "Clinica Smile",
  "address": "Str. Zâmbetului 12"
}
```

#### `GET /api/v1/clinics` – Listează clinicile utilizatorului


### 🏥 Clinici

#### `POST /api/v1/invitations` – Creează invitație
```json
{
  "clinicId": "uuid-clinic",
  "role": "ASSISTANT",
  "doctorId": "uuid-doctor" // opțional
}
```

#### `GET /api/v1/invitations/validate?token=...` – Validează token-ul


### 👤 Utilizatori

#### `GET /api/v1/users/me` – Datele userului curent

#### `DELETE /api/v1/users/me` – Șterge userul curent

### 📦 Produse (stocuri)

#### `POST /api/v1/products` – Adaugă produs
```json
{
  "name": "Lidocain",
  "category": "Anestezice",
  "unit": "ml",
  "quantity": 10,
  "lowStockThreshold": 3,
  "expirationDate": "2025-12-01",
  "clinicId": "uuid-clinic"
}
```

#### `POST /api/products/{productId}/stock` –  Actualizare stoc
```json
{
  "actionType": "OUT",
  "quantity": 2,
  "reason": "Folosit tratament carie"
}
```

#### `GET /api/v1/products/clinic/{clinicId}` – Produse din clinică

#### `GET /api/v1/products/clinic/{clinicId}/low-stock` – Produse sub limită

### 👤 Utilizatori

#### `GET /api/v1/users/me` – Datele userului curent


## 🧾 Inventory Logs (istoric stocuri)

#### `GET /api/v1/inventory-logs/product/{productId}` – Loguri pentru produs

#### `GET /api/v1/inventory-logs/user/{userId}` – Loguri pentru utilizator

#### `GET /api/v1/inventory-logs/clinic/{clinicId}` – Loguri pentru clinică

#### `GET /api/v1/inventory-logs/clinic/{clinicId}/export` – Export Excel

### 📅 Programări

#### `POST /api/appointments` – Creează programare
```json
{
  "clinicId": "uuid-clinic",
  "dateTime": "2025-05-12T10:30:00",
  "durationMinutes": 30,
  "patientName": "Andrei Vasile",
  "reason": "Consultație"
}
```
#### `GET /api/v1/appointments/clinic/{clinicId}` – Programări clinică

#### `GET /api/v1/appointments/doctor` – Programările mele (doctor curent)

#### `PATCH /api/v1/appointments/{appointmentId}/status` – Actualizează statusul
```json
{
  "status": "COMPLETED" // sau "CANCELED"
}
```

### 📊 Dashboard clinică

#### `GET /api/v1/dashboard/clinic/{clinicId}` – Statistici lunare
```json
{
  "totalAppointments": 14,
  "completedAppointments": 10,
  "canceledAppointments": 2,
  "lowStockCount": 3,
  "expiredCount": 1,
  "consumptionLogsThisMonth": 18,
  "lowStockProducts": [...],
  "expiredProducts": [...]
}
```

1. Clonează proiectul:
   ```bash
   git clone https://github.com/username/dentatrack-backend.git
   cd dentatrack-backend
