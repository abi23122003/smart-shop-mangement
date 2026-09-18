# Smart Shop Manager

Smart Shop Manager is a full-stack retail and inventory management application built with a Spring Boot backend and a React/Vite frontend.

---

## Configuration & Secrets Management

To run the backend securely without committing sensitive secrets, configure the following environment variables or create a local properties file:

### Environment Variables

| Variable | Description | Default | Required in Prod |
|---|---|---|---|
| `DB_URL` | JDBC Database URL | `jdbc:mysql://localhost:3306/shop_manager` | No |
| `DB_USERNAME` | Database username | `root` | No |
| `DB_PASSWORD` | Database password | None | **Yes** |
| `JWT_SECRET` | Secret key for signing JWTs (must be >= 256 bits / 32 chars) | None | **Yes** |
| `JWT_EXPIRATION_MS` | JWT expiration duration in milliseconds | `86400000` (24h) | No |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed CORS origins | `http://localhost:5173,http://localhost:3000,http://127.0.0.1:5173,http://127.0.0.1:3000` | No |

### Local Properties Setup (Alternative)

You can create an `application-local.properties` file in `Smart-Shop-Manager/backend/src/main/resources/` (which is gitignored):

```properties
spring.datasource.password=your_db_password
jwt.secret=your_super_secret_jwt_key_at_least_32_characters_long
```

Then run the application with the `local` profile or provide the env vars directly.

---

## Running Locally

### Backend (Spring Boot)

```bash
cd Smart-Shop-Manager/backend
# Set required env vars or use local properties:
$env:DB_PASSWORD="your_password"
$env:JWT_SECRET="your_secure_256_bit_jwt_secret_key_here_12345"
.\mvnw.cmd spring-boot:run
```

### Frontend (React + Vite)

```bash
cd Smart-Shop-Manager/frontend
npm install
npm run dev
```
