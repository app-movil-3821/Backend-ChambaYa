# Backend ChambaYa

Backend desarrollado para la aplicación móvil **ChambaYa**, una plataforma que conecta MYPEs/contratantes con jóvenes chambeadores para cubrir trabajos temporales o turnos cortos.

El backend está desarrollado con **Spring Boot**, **Java 21**, **MongoDB** y una arquitectura basada en **Domain-Driven Design (DDD)** mediante Bounded Contexts.

---

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data MongoDB
- Spring Security
- BCrypt Password Encoder
- Swagger / OpenAPI
- Maven
- MongoDB Compass

---

## Arquitectura del backend

El proyecto está organizado por Bounded Contexts:

```txt
src/main/java/com/chambaya/backend/
├── iam
├── jobs
├── enrollments
├── reputation
└── shared
```

Cada contexto sigue una estructura basada en DDD:

```txt
domain
application
infrastructure
interfaces
```

---

## Bounded Contexts implementados

### IAM Context

Gestiona usuarios, roles y perfiles.

Funciones principales:

- Registrar usuarios.
- Diferenciar roles: `CHAMBEADOR` y `CONTRATANTE`.
- Guardar contraseña con BCrypt.
- Consultar usuarios.
- Actualizar perfil.

Colección MongoDB:

```txt
users
```

---

### Jobs Context

Gestiona los trabajos o turnos publicados por los contratantes.

Funciones principales:

- Crear jobs.
- Listar jobs.
- Buscar jobs por contratante.
- Buscar jobs publicados.
- Cambiar estado del job.
- Consultar jobs cercanos por coordenadas.
- Validar que solo un usuario `CONTRATANTE` pueda crear jobs.

Colección MongoDB:

```txt
jobs
```

---

### Enrollments Context

Gestiona las postulaciones de chambeadores a jobs.

Funciones principales:

- Postular a un job.
- Aceptar postulación.
- Rechazar postulación.
- Cancelar postulación.
- Validar que el worker sea `CHAMBEADOR`.
- Validar que el contractor sea `CONTRATANTE`.
- Validar que el contractor sea dueño del job.
- Cambiar el job a `MATCHED` cuando se acepta una postulación.
- Rechazar automáticamente otras postulaciones pendientes del mismo job.

Colección MongoDB:

```txt
enrollments
```

> Nota: En el documento del proyecto este contexto aparece como **Application Context**. En la implementación backend se nombró como **Enrollments Context** para evitar confusión con la capa `application` y representar mejor el proceso de postulación.

---

### Reputation Context

Gestiona las reseñas y calificaciones entre usuarios.

Funciones principales:

- Crear reviews.
- Validar rating entre 1 y 5.
- Consultar reviews por usuario.
- Consultar reviews por job.
- Consultar promedio de reputación.
- Evitar reviews duplicadas para el mismo job y usuarios.
- Validar que el reviewer y reviewed user existan.
- Evitar que un usuario se califique a sí mismo.

Colección MongoDB:

```txt
reviews
```

---

## Configuración local

El proyecto utiliza MongoDB local.

Archivo de configuración:

```txt
src/main/resources/application.properties
```

Configuración actual:

```properties
spring.application.name=backend
server.port=8080
spring.mongodb.uri=mongodb://localhost:27017/chambaya_db
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## Requisitos previos

Antes de ejecutar el backend, asegúrate de tener instalado:

- Java 21
- MongoDB Community Server
- MongoDB Compass
- IntelliJ IDEA o IDE compatible
- Git

El proyecto incluye Maven Wrapper, por lo que no es obligatorio instalar Maven de forma global.

---

## Ejecutar el proyecto

Desde la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
.\mvnw.cmd spring-boot:run
```

También se puede ejecutar directamente desde IntelliJ IDEA ejecutando la clase principal:

```txt
BackendApplication.java
```

---

## Ejecutar pruebas de compilación

En Windows:

```bash
.\mvnw.cmd test
```

Si todo está correcto, debe aparecer:

```txt
BUILD SUCCESS
```

---

## Swagger

Una vez levantado el backend, abrir:

```txt
http://localhost:8080/swagger-ui.html
```

Desde Swagger se pueden probar los endpoints del backend.

---

## Flujo principal de prueba

### 1. Crear usuario contratante

Endpoint:

```txt
POST /api/v1/users
```

Body:

```json
{
  "name": "Rosa Mendoza",
  "email": "rosa.mendoza.negocio@gmail.com",
  "password": "123456",
  "role": "CONTRATANTE",
  "skills": [],
  "experience": "Dueña de cafetería",
  "district": "Miraflores",
  "phone": "987111222"
}
```

Copiar el `id` generado como `contractorId`.

---

### 2. Crear usuario chambeador

Endpoint:

```txt
POST /api/v1/users
```

Body:

```json
{
  "name": "Diego Salazar",
  "email": "diego.salazar.worker@gmail.com",
  "password": "123456",
  "role": "CHAMBEADOR",
  "skills": ["atención al cliente", "limpieza", "rapidez"],
  "experience": "Apoyo en cafeterías y restaurantes",
  "district": "Miraflores",
  "phone": "911222333"
}
```

Copiar el `id` generado como `workerId`.

---

### 3. Crear job

Endpoint:

```txt
POST /api/v1/jobs
```

Body:

```json
{
  "contractorId": "ID_DEL_CONTRATANTE",
  "title": "Apoyo para atención en cafetería",
  "description": "Se necesita apoyo para atención de clientes y limpieza básica.",
  "category": "Atención al cliente",
  "requiredSkills": ["atención al cliente", "limpieza", "rapidez"],
  "paymentAmount": 60,
  "latitude": -12.1211,
  "longitude": -77.0305,
  "address": "Av. Larco 450",
  "district": "Miraflores",
  "scheduledStart": "2026-05-08T16:00:00",
  "scheduledEnd": "2026-05-08T21:00:00"
}
```

Copiar el `id` generado como `jobId`.

El job se crea inicialmente con estado:

```txt
PUBLISHED
```

---

### 4. Crear postulación

Endpoint:

```txt
POST /api/v1/enrollments
```

Body:

```json
{
  "jobId": "ID_DEL_JOB",
  "workerId": "ID_DEL_CHAMBEADOR",
  "contractorId": "ID_DEL_CONTRATANTE"
}
```

La postulación se crea con estado:

```txt
PENDING
```

---

### 5. Aceptar postulación

Endpoint:

```txt
PUT /api/v1/enrollments/{id}/accept
```

Resultado esperado:

- La postulación aceptada cambia a `ACCEPTED`.
- El job relacionado cambia a `MATCHED`.
- Las otras postulaciones pendientes del mismo job cambian a `REJECTED`.

---

### 6. Crear review

Endpoint:

```txt
POST /api/v1/reviews
```

Body:

```json
{
  "jobId": "ID_DEL_JOB",
  "reviewerId": "ID_DEL_CONTRATANTE",
  "reviewedUserId": "ID_DEL_CHAMBEADOR",
  "rating": 5,
  "comment": "Cumplió correctamente con el turno y tuvo buena actitud."
}
```

---

### 7. Consultar reputación

Endpoint:

```txt
GET /api/v1/reviews/user/{userId}/summary
```

Resultado esperado:

```json
{
  "userId": "ID_DEL_USUARIO",
  "averageRating": 5.0,
  "totalReviews": 1
}
```

---

## Endpoint para mapa

El backend permite consultar jobs cercanos usando coordenadas:

```txt
GET /api/v1/jobs/nearby?latitude=-12.1211&longitude=-77.0305&radiusKm=10
```

Este endpoint devuelve jobs disponibles cercanos para que la aplicación móvil pueda mostrarlos en un mapa.

Estados considerados disponibles:

```txt
PUBLISHED
REOPENED
```

La lógica actual utiliza las coordenadas almacenadas en cada job y calcula la distancia aproximada entre puntos. La integración con Google Maps API queda como mejora posterior.

---

## Manejo de errores

El backend cuenta con un `GlobalExceptionHandler`, que permite devolver errores en formato JSON.

Ejemplo:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Worker already applied to this job",
  "path": "/api/v1/enrollments",
  "timestamp": "2026-05-08T14:30:00"
}
```

---

## Colecciones en MongoDB

El backend crea las siguientes colecciones:

```txt
users
jobs
enrollments
reviews
```

Estas colecciones se crean automáticamente cuando se insertan los primeros documentos.

---

## Estado actual del backend

Implementado:

- Proyecto Spring Boot base.
- MongoDB local.
- Swagger.
- Estructura DDD por Bounded Contexts.
- IAM Context.
- Jobs Context.
- Enrollments Context.
- Reputation Context.
- Manejo global de errores.
- Password hashing con BCrypt.
- Validaciones cruzadas básicas.
- Flujo principal funcional.
- Endpoint base para mapa.

Pendiente:

- JWT real.
- Deploy.
- MongoDB Atlas.
- Chat interno.
- Notificaciones.
- Payment Context.
- Integración avanzada con Google Maps API.