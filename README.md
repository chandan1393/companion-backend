# Companion Marketplace — Spring Boot Backend

Backend for the Companion Angular marketplace.

## Architecture

The project intentionally uses a straightforward **package-by-feature** structure:

```text
com.xelvo.companion
├── auth
├── user
├── companion
├── experience
├── booking
├── availability
├── review
├── message
├── payment
├── favorite
├── admin
├── security
├── common
└── config
```

Each feature is split into:

```text
controller -> service -> repository -> entity
                 |
                DTO
```

This is deliberately not over-engineered. It makes debugging easy in Eclipse/IntelliJ because each request has a predictable path.

## Technology

- Java 17
- Spring Boot 3.5
- Spring MVC
- Spring Security
- JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway
- Bean Validation
- Actuator
- Maven

No Lombok is used. Constructors, fields, getters and state-changing methods are explicit so debugging is easier.

## Database

PostgreSQL database:

```text
database: companion
username: postgres
password: postgres
port: 5432
```

The application uses:

```yaml
spring.jpa.hibernate.ddl-auto: validate
```

Flyway owns schema changes.

Migration:

```text
src/main/resources/db/migration/V1__create_schema.sql
```

Do not change `ddl-auto` to `update` in production.

## Start PostgreSQL

If Docker is available:

```powershell
docker compose up -d postgres
```

Then run:

```powershell
mvn spring-boot:run
```

The API base URL is:

```text
http://localhost:8080/api
```

## Demo accounts

| Role | Email | Password |
|---|---|---|
| Customer | user@companion.demo | user123 |
| Customer | rohan@companion.demo | user123 |
| Customer | ananya@companion.demo | user123 |
| Companion | companion@companion.demo | companion123 |
| Companion | arjun@companion.demo | companion123 |
| Companion | simran@companion.demo | companion123 |
| Companion | kabir@companion.demo | companion123 |
| Admin | admin@companion.demo | admin123 |

The demo seeder is idempotent: it creates any missing demo accounts, companions, experiences, availability, bookings, payments, reviews, favorites and messages without requiring an empty database.

## Main API map

### Authentication

```text
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/me
```

### Public discovery

```text
GET /api/public/companions
GET /api/public/companions/{id}
GET /api/public/companions/{id}/experiences
GET /api/public/companions/{id}/availability
GET /api/public/companions/{id}/reviews
GET /api/public/experiences
GET /api/public/cities
GET /api/public/categories
```

### Customer

```text
GET  /api/users/me
PUT  /api/users/me

GET  /api/bookings/mine
POST /api/bookings
GET  /api/bookings/{id}
PATCH /api/bookings/{id}/status

GET  /api/favorites
POST /api/favorites/{companionId}
DELETE /api/favorites/{companionId}
GET /api/favorites/{companionId}/exists

GET  /api/messages
GET  /api/messages/conversation/{otherUserId}
POST /api/messages
PATCH /api/messages/{id}/read
GET /api/messages/unread-count

GET /api/payments/booking/{bookingId}
POST /api/payments/booking/{bookingId}/order
POST /api/payments/booking/{bookingId}/confirm-mock

POST /api/reviews
```

### Companion

```text
GET  /api/companion/profile
PUT  /api/companion/profile
PUT  /api/companion/profile/interests

POST /api/companion/profile/photos/upload
POST /api/companion/profile/photos
DELETE /api/companion/profile/photos

GET  /api/companion/experiences
POST /api/companion/experiences
PUT  /api/companion/experiences/{id}
DELETE /api/companion/experiences/{id}

GET  /api/companion/availability
POST /api/companion/availability
DELETE /api/companion/availability/{id}

GET /api/bookings/companion
PATCH /api/bookings/{id}/status
```

### Admin

```text
GET /api/admin/dashboard
GET /api/admin/users
PATCH /api/admin/users/{id}/status
GET /api/admin/companions
PATCH /api/admin/companions/{id}/status
GET /api/admin/bookings
```

## Authentication

Login response:

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "...",
    "userId": 1,
    "fullName": "Chandan Sharma",
    "email": "user@companion.demo",
    "role": "USER"
  }
}
```

Send it on protected requests:

```http
Authorization: Bearer <token>
```

The Angular frontend should store the token and add it through an `HttpInterceptor`.

## Booking rules

The service checks:

1. Companion exists.
2. Experience exists.
3. Experience belongs to the companion.
4. Experience is active.
5. Requested duration is calculated from the experience.
6. Existing PENDING/CONFIRMED booking does not overlap.
7. Companion has an AVAILABLE slot covering the requested time.
8. Platform fee is calculated server-side.
9. Payment record is created together with the booking.

This keeps price and booking validation on the backend rather than trusting Angular.

## Payment

The current payment implementation has a provider boundary in:

```text
payment/service/PaymentService.java
```

The `confirm-mock` endpoint is only for frontend development.

For production, replace provider-order creation/confirmation with Razorpay (or another provider) and verify the provider signature/webhook on the server.

Never trust a payment-success flag sent by Angular.

## Image upload

Companion photo upload:

```http
POST /api/companion/profile/photos/upload
Content-Type: multipart/form-data
Authorization: Bearer <token>
```

Field:

```text
file
```

Rules:

- JPG / PNG / WEBP
- maximum 5 MB
- maximum 6 profile photos
- generated UUID filename
- path traversal protection
- files are served below `/api/uploads/**`

For production, move storage to S3/Cloudinary/object storage rather than local disk.

## Debugging approach

When an API fails, follow this path:

```text
Angular
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
PostgreSQL
```

Business rules live in services.

Database access lives in repositories.

Request/response classes are DTOs.

Entities are not returned directly from controllers.

`GlobalExceptionHandler` gives consistent JSON errors:

```json
{
  "success": false,
  "message": "Selected time is already booked",
  "path": "/api/bookings",
  "timestamp": "...",
  "details": null
}
```

## Production checklist

Before going live:

- Set a strong `JWT_SECRET`.
- Use managed PostgreSQL.
- Configure HTTPS.
- Configure real CORS origin.
- Replace mock payment flow with Razorpay server-side verification.
- Move images to object storage.
- Add rate limiting.
- Add refresh tokens if long sessions are required.
- Add email/notification service.
- Add Redis only when there is an actual caching/session use case.
- Add WebSocket only when real-time chat is required.
- Add audit logging for admin actions.
- Add integration tests around booking/payment state transitions.
