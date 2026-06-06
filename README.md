# Backend ChambaYa

Backend del proyecto **ChambaYa**, una plataforma móvil orientada a conectar contratantes con chambeadores para trabajos temporales o por turnos.

El backend fue desarrollado con **Java Spring Boot**, siguiendo una estructura organizada por contextos de dominio. Expone una API REST documentada con Swagger y utiliza MongoDB como base de datos.

---

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Security
* JWT
* MongoDB
* Maven
* Swagger / OpenAPI
* Railway / despliegue cloud opcional

---

## Contextos implementados

### IAM

Gestión de usuarios, autenticación y roles.

Funcionalidades principales:

* Registro de usuarios
* Login con JWT
* Encriptación de contraseñas con BCrypt
* Roles: `CHAMBEADOR` y `CONTRATANTE`
* Verificación automática por correo institucional
* Protección de endpoints por rol

Regla de verificación:

```txt
@upc.edu.pe → verified = true
@alumno.upc.edu.pe → verified = true
otros correos → verified = false
```

---

### Jobs

Gestión de trabajos publicados por contratantes.

Funcionalidades principales:

* Crear trabajos
* Consultar trabajos
* Buscar trabajos cercanos por ubicación
* Filtrar por categoría, distrito, pago mínimo, pago máximo y fecha
* Ciclo de vida del trabajo

Flujo principal del estado del trabajo:

```txt
PUBLISHED → MATCHED → IN_PROGRESS → COMPLETED
```

---

### Enrollments

Gestión de postulaciones de chambeadores a trabajos.

Funcionalidades principales:

* Postular a un trabajo
* Aceptar postulación
* Rechazar postulación
* Cancelar postulación
* Al aceptar una postulación:

    * El job pasa a `MATCHED`
    * Se rechazan otras postulaciones pendientes
    * Se genera una notificación
    * Se crea una conversación entre contratante y chambeador

---

### Reputation

Gestión de reseñas y calificaciones.

Funcionalidades principales:

* Crear review
* Consultar reviews por usuario, job o reviewer
* Obtener resumen de calificación
* Validar que solo se pueda crear una review cuando el job esté `COMPLETED`

---

### Notifications

Gestión de notificaciones internas.

Funcionalidades principales:

* Crear notificaciones automáticas
* Consultar notificaciones por usuario
* Consultar notificaciones no leídas
* Marcar una notificación como leída
* Marcar todas las notificaciones de un usuario como leídas

Eventos que generan notificaciones:

* Nueva postulación recibida
* Postulación aceptada
* Postulación rechazada
* Postulación cancelada

---

### Communication

Gestión de conversaciones y mensajes.

Funcionalidades principales:

* Crear conversación
* Crear conversación automáticamente al aceptar una postulación
* Consultar conversaciones por usuario
* Consultar mensajes de una conversación
* Enviar mensajes
* Cerrar conversación

---

### Payments

Gestión de pagos simulados.

Funcionalidades principales:

* Crear pago para un job completado
* Confirmar pago
* Cancelar pago
* Consultar pagos por job, worker o contractor

Reglas principales:

* Solo se puede crear un pago si el job está `COMPLETED`
* El pago inicia en estado `PENDING`
* Un pago confirmado no puede cancelarse

Métodos de pago disponibles:

```txt
CASH
YAPE
PLIN
BANK_TRANSFER
```

---

### Favorites

Gestión de trabajos favoritos para chambeadores.

Funcionalidades principales:

* Guardar un job como favorito
* Consultar favoritos por chambeador
* Eliminar favorito
* Evitar duplicados del mismo job para el mismo chambeador

---

## Requisitos para ejecutar el proyecto

Antes de ejecutar el backend, se necesita tener instalado:

* Java 21
* Maven
* MongoDB local o una conexión a MongoDB Atlas
* IntelliJ IDEA o un IDE compatible

---

## Variables de entorno

El backend utiliza variables de entorno para la conexión a la base de datos y configuración de seguridad.

Ejemplo:

```properties
MONGODB_URI=mongodb+srv://usuario:password@cluster.mongodb.net/chambaya_db
JWT_SECRET=clave-secreta-del-token
```

En entorno local, también puede configurarse desde `application.properties`.

---

## Ejecución local

Desde la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

También se puede ejecutar directamente desde IntelliJ mediante la clase:

```txt
BackendApplication
```

---

## Swagger

Swagger permite probar todos los endpoints del backend.

URL local:

```txt
http://localhost:8080/swagger-ui.html
```

Para endpoints protegidos, primero se debe iniciar sesión y luego pegar el token en el botón **Authorize** con el formato:

```txt
Bearer TOKEN
```

---

## Autenticación

### Login

```http
POST /api/v1/auth/login
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

El token debe enviarse en los endpoints protegidos:

```txt
Authorization: Bearer TOKEN
```

---

## Principales endpoints

### Auth

```http
POST /api/v1/auth/login
```

### Users

```http
POST /api/v1/users
GET /api/v1/users
GET /api/v1/users/{id}
PUT /api/v1/users/{id}/profile
```

### Jobs

```http
POST /api/v1/jobs
GET /api/v1/jobs
GET /api/v1/jobs/{id}
GET /api/v1/jobs/published
GET /api/v1/jobs/contractor/{contractorId}
GET /api/v1/jobs/nearby
PUT /api/v1/jobs/{id}/start
PUT /api/v1/jobs/{id}/complete
PUT /api/v1/jobs/{id}/cancel
PUT /api/v1/jobs/{id}/publish
PUT /api/v1/jobs/{id}/close
PUT /api/v1/jobs/{id}/reopen
```

### Enrollments

```http
POST /api/v1/enrollments
GET /api/v1/enrollments
GET /api/v1/enrollments/{id}
GET /api/v1/enrollments/job/{jobId}
GET /api/v1/enrollments/worker/{workerId}
GET /api/v1/enrollments/contractor/{contractorId}
GET /api/v1/enrollments/pending
PUT /api/v1/enrollments/{id}/accept
PUT /api/v1/enrollments/{id}/reject
PUT /api/v1/enrollments/{id}/cancel
```

### Reviews

```http
POST /api/v1/reviews
GET /api/v1/reviews
GET /api/v1/reviews/{id}
GET /api/v1/reviews/job/{jobId}
GET /api/v1/reviews/reviewer/{reviewerId}
GET /api/v1/reviews/user/{userId}
GET /api/v1/reviews/user/{userId}/summary
```

### Notifications

```http
GET /api/v1/notifications
GET /api/v1/notifications/user/{userId}
GET /api/v1/notifications/user/{userId}/unread
PUT /api/v1/notifications/{id}/read
PUT /api/v1/notifications/user/{userId}/read-all
```

### Communication

```http
POST /api/v1/communications
GET /api/v1/communications
GET /api/v1/communications/{id}
GET /api/v1/communications/user/{userId}
GET /api/v1/communications/job/{jobId}
GET /api/v1/communications/{conversationId}/messages
POST /api/v1/communications/{conversationId}/messages
PUT /api/v1/communications/{conversationId}/close
```

### Payments

```http
POST /api/v1/payments
GET /api/v1/payments
GET /api/v1/payments/{id}
GET /api/v1/payments/job/{jobId}
GET /api/v1/payments/worker/{workerId}
GET /api/v1/payments/contractor/{contractorId}
PUT /api/v1/payments/{id}/confirm
PUT /api/v1/payments/{id}/cancel
```

### Favorites

```http
POST /api/v1/favorites
GET /api/v1/favorites
GET /api/v1/favorites/{id}
GET /api/v1/favorites/worker/{workerId}
DELETE /api/v1/favorites/{id}
```

---

## Integración con aplicación móvil

La guía detallada para conectar el backend con la app móvil se encuentra en:

```txt
docs/mobile-integration.md
```

Incluye:

* URL base para emulador Android
* URL base para navegador local
* Uso del token JWT
* Endpoints principales
* Ejemplos de request y response JSON

Para Android Emulator, la base URL recomendada es:

```txt
http://10.0.2.2:8080/api/v1/
```

---

## Flujo principal de prueba

Flujo recomendado para validar el sistema completo:

```txt
1. Registrar un CONTRATANTE
2. Registrar un CHAMBEADOR
3. Iniciar sesión y obtener JWT
4. CONTRATANTE crea un job
5. CHAMBEADOR postula al job
6. CONTRATANTE acepta la postulación
7. Se genera notificación y conversación
8. Job pasa a MATCHED
9. Job se inicia con start
10. Job se completa con complete
11. Se crea review
12. Se crea payment
13. Se confirma payment
14. CHAMBEADOR puede guardar jobs como favoritos
```

---

## Estado del backend

Funcionalidades completadas:

```txt
JWT + login real
Protección por roles
Ciclo de vida del Job
Reviews condicionadas a jobs completados
Notifications Context
Communication / Chat Context
Filtros para mapa y trabajos cercanos
Payment Context simulado
Favorites Context
Verificación por correo institucional
Documentación de integración móvil
```

Pendientes o mejoras futuras:

```txt
Pruebas unitarias e integración más completas
Integración real con Google OAuth
Integración real con pasarela de pagos
Despliegue final en un servicio cloud
Mejoras de seguridad para producción
```
