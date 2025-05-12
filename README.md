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

> Prefix comun: `/api`  
> Toate endpoint-urile necesită autentificare (`Authorization: Bearer <token>`)  
> Datele sunt în format `application/json`

---

### 🔐 Autentificare

#### `POST /api/auth/register`
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



## ⚙️ Configurare locală

1. Clonează proiectul:
   ```bash
   git clone https://github.com/username/dentatrack-backend.git
   cd dentatrack-backend
