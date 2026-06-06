# Mobile Integration Guide - ChambaYa Backend

## 1. Base URL

### Android Emulator

```txt
http://10.0.2.2:8080/api/v1/
```

### Navegador o Swagger local

```txt
http://localhost:8080/api/v1/
```

Swagger:

```txt
http://localhost:8080/swagger-ui.html
```

### Celular físico en la misma red WiFi

```txt
http://IP_DE_LA_PC:8080/api/v1/
```

Ejemplo:

```txt
http://192.168.1.25:8080/api/v1/
```

---

## 2. Autenticación

### Login

```http
POST /auth/login
```

Request:

```json
{
  "email": "usuario@example.com",
  "password": "123456"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1Ni...",
  "userId": "USER_ID",
  "name": "Sebastian",
  "email": "usuario@example.com",
  "role": "CHAMBEADOR"
}
```

Para consumir endpoints protegidos, enviar el token en el header:

```txt
Authorization: Bearer TOKEN
```

---

## 3. Users / IAM

### Registrar usuario

```http
POST /users
```

Request:

```json
{
  "name": "Sebastian",
  "email": "u202111041@upc.edu.pe",
  "password": "123456",
  "role": "CHAMBEADOR",
  "skills": ["atención al cliente", "limpieza"],
  "experience": "1 año de experiencia",
  "district": "Miraflores",
  "phone": "900000001"
}
```

Response:

```json
{
  "id": "USER_ID",
  "name": "Sebastian",
  "email": "u202111041@upc.edu.pe",
  "role": "CHAMBEADOR",
  "profile": {
    "photoUrl": null,
    "skills": ["atención al cliente", "limpieza"],
    "experience": "1 año de experiencia",
    "district": "Miraflores",
    "phone": "900000001",
    "verified": true
  },
  "createdAt": "2026-06-03T10:00:00",
  "updatedAt": "2026-06-03T10:00:00"
}
```

Nota: si el correo termina en `@upc.edu.pe` o `@alumno.upc.edu.pe`, el campo `verified` será `true`. Con otros correos será `false`.

---

## 4. Jobs

### Crear trabajo

Requiere token de `CONTRATANTE`.

```http
POST /jobs
```

Request:

```json
{
  "contractorId": "CONTRACTOR_ID",
  "title": "Ayudante para turno de noche",
  "description": "Apoyo en atención y limpieza del local",
  "category": "Atención al cliente",
  "requiredSkills": ["responsabilidad", "puntualidad"],
  "paymentAmount": 60,
  "latitude": -12.1211,
  "longitude": -77.0305,
  "address": "Av. Larco 450",
  "district": "Miraflores",
  "scheduledStart": "2026-06-05T18:00:00",
  "scheduledEnd": "2026-06-05T23:00:00"
}
```

Response:

```json
{
  "id": "JOB_ID",
  "contractorId": "CONTRACTOR_ID",
  "title": "Ayudante para turno de noche",
  "description": "Apoyo en atención y limpieza del local",
  "category": "Atención al cliente",
  "requiredSkills": ["responsabilidad", "puntualidad"],
  "paymentAmount": 60,
  "location": {
    "latitude": -12.1211,
    "longitude": -77.0305,
    "address": "Av. Larco 450",
    "district": "Miraflores"
  },
  "scheduledStart": "2026-06-05T18:00:00",
  "scheduledEnd": "2026-06-05T23:00:00",
  "status": "PUBLISHED",
  "createdAt": "2026-06-03T10:00:00",
  "updatedAt": "2026-06-03T10:00:00"
}
```

### Consultar trabajos

```http
GET /jobs
GET /jobs/{id}
GET /jobs/published
GET /jobs/contractor/{contractorId}
```

### Buscar trabajos cercanos

```http
GET /jobs/nearby
```

Query params obligatorios:

```txt
latitude
longitude
radiusKm
```

Query params opcionales:

```txt
category
district
minPayment
maxPayment
scheduledDate
```

Ejemplo:

```http
GET /jobs/nearby?latitude=-12.1211&longitude=-77.0305&radiusKm=10&district=Miraflores&minPayment=50
```

### Ciclo de vida del trabajo

```http
PUT /jobs/{id}/start
PUT /jobs/{id}/complete
PUT /jobs/{id}/cancel
PUT /jobs/{id}/publish
PUT /jobs/{id}/close
PUT /jobs/{id}/reopen
```

Flujo principal:

```txt
PUBLISHED → MATCHED → IN_PROGRESS → COMPLETED
```

---

## 5. Enrollments

### Postular a un trabajo

Requiere token de `CHAMBEADOR`.

```http
POST /enrollments
```

Request:

```json
{
  "jobId": "JOB_ID",
  "workerId": "WORKER_ID",
  "contractorId": "CONTRACTOR_ID"
}
```

Response:

```json
{
  "id": "ENROLLMENT_ID",
  "jobId": "JOB_ID",
  "workerId": "WORKER_ID",
  "contractorId": "CONTRACTOR_ID",
  "status": "PENDING",
  "appliedAt": "2026-06-03T10:00:00",
  "respondedAt": null,
  "updatedAt": "2026-06-03T10:00:00"
}
```

### Acciones

```http
PUT /enrollments/{id}/accept
PUT /enrollments/{id}/reject
PUT /enrollments/{id}/cancel
```

Cuando se acepta una postulación:

```txt
- Enrollment pasa a ACCEPTED.
- Job pasa a MATCHED.
- Se crea una notificación.
- Se crea una conversación.
```

### Consultas

```http
GET /enrollments
GET /enrollments/{id}
GET /enrollments/job/{jobId}
GET /enrollments/worker/{workerId}
GET /enrollments/contractor/{contractorId}
GET /enrollments/pending
```

---

## 6. Notifications

### Consultar notificaciones de usuario

```http
GET /notifications/user/{userId}
```

Response:

```json
[
  {
    "id": "NOTIFICATION_ID",
    "userId": "USER_ID",
    "title": "Nueva postulación",
    "message": "Un chambeador postuló al trabajo: Ayudante para turno de noche",
    "type": "ENROLLMENT_RECEIVED",
    "read": false,
    "createdAt": "2026-06-03T10:00:00",
    "readAt": null
  }
]
```

### Otros endpoints

```http
GET /notifications/user/{userId}/unread
PUT /notifications/{id}/read
PUT /notifications/user/{userId}/read-all
```

---

## 7. Communication / Chat

### Consultar conversaciones de usuario

```http
GET /communications/user/{userId}
```

Response:

```json
[
  {
    "id": "CONVERSATION_ID",
    "jobId": "JOB_ID",
    "enrollmentId": "ENROLLMENT_ID",
    "contractorId": "CONTRACTOR_ID",
    "workerId": "WORKER_ID",
    "status": "ACTIVE",
    "createdAt": "2026-06-03T10:00:00",
    "updatedAt": "2026-06-03T10:00:00"
  }
]
```

### Enviar mensaje

```http
POST /communications/{conversationId}/messages
```

Request:

```json
{
  "senderId": "USER_ID",
  "content": "Hola, coordinemos los detalles del turno."
}
```

Response:

```json
{
  "id": "MESSAGE_ID",
  "conversationId": "CONVERSATION_ID",
  "senderId": "USER_ID",
  "content": "Hola, coordinemos los detalles del turno.",
  "sentAt": "2026-06-03T10:00:00",
  "read": false,
  "readAt": null
}
```

### Otros endpoints

```http
GET /communications/{conversationId}/messages
GET /communications/job/{jobId}
PUT /communications/{conversationId}/close
```

---

## 8. Reviews

Solo se puede crear una review si el job está `COMPLETED`.

```http
POST /reviews
```

Request:

```json
{
  "jobId": "JOB_ID",
  "reviewerId": "USER_ID_QUE_CALIFICA",
  "reviewedUserId": "USER_ID_CALIFICADO",
  "rating": 5,
  "comment": "Buen trabajo y responsabilidad."
}
```

Response:

```json
{
  "id": "REVIEW_ID",
  "jobId": "JOB_ID",
  "reviewerId": "USER_ID_QUE_CALIFICA",
  "reviewedUserId": "USER_ID_CALIFICADO",
  "rating": 5,
  "comment": "Buen trabajo y responsabilidad.",
  "createdAt": "2026-06-03T10:00:00"
}
```

Consultas:

```http
GET /reviews
GET /reviews/{id}
GET /reviews/job/{jobId}
GET /reviews/reviewer/{reviewerId}
GET /reviews/user/{userId}
GET /reviews/user/{userId}/summary
```

---

## 9. Payments

Solo se puede crear un payment si el job está `COMPLETED`.

```http
POST /payments
```

Request:

```json
{
  "jobId": "JOB_ID",
  "method": "YAPE"
}
```

Métodos permitidos:

```txt
CASH
YAPE
PLIN
BANK_TRANSFER
```

Response:

```json
{
  "id": "PAYMENT_ID",
  "jobId": "JOB_ID",
  "enrollmentId": "ENROLLMENT_ID",
  "contractorId": "CONTRACTOR_ID",
  "workerId": "WORKER_ID",
  "amount": 60,
  "method": "YAPE",
  "status": "PENDING",
  "createdAt": "2026-06-03T10:00:00",
  "confirmedAt": null,
  "cancelledAt": null
}
```

Acciones:

```http
PUT /payments/{id}/confirm
PUT /payments/{id}/cancel
```

Consultas:

```http
GET /payments
GET /payments/{id}
GET /payments/job/{jobId}
GET /payments/worker/{workerId}
GET /payments/contractor/{contractorId}
```

---

## 10. Favorites

### Guardar favorito

Requiere token de `CHAMBEADOR`.

```http
POST /favorites
```

Request:

```json
{
  "workerId": "WORKER_ID",
  "jobId": "JOB_ID"
}
```

Response:

```json
{
  "id": "FAVORITE_ID",
  "workerId": "WORKER_ID",
  "jobId": "JOB_ID",
  "createdAt": "2026-06-03T10:00:00"
}
```

### Consultar favoritos

```http
GET /favorites/worker/{workerId}
```

### Eliminar favorito

```http
DELETE /favorites/{id}
```

Response:

```txt
204 No Content
```

---

## 11. Formato de errores

El backend devuelve errores con esta estructura:

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Job is already saved as favorite by this worker",
  "path": "/api/v1/favorites",
  "timestamp": "2026-06-03T10:00:00"
}
```

Códigos comunes:

```txt
400 Bad Request → datos inválidos
401 Unauthorized → token inválido o credenciales incorrectas
403 Forbidden → rol no permitido
404 Not Found → recurso no encontrado
409 Conflict → regla de negocio no permitida
```
