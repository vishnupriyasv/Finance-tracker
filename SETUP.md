# Environment Setup Guide

## Backend Configuration

### application.properties
```properties
# Server Configuration
spring.application.name=finance-tracker
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=root
spring.datasource.password=1234

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
jwt.secret=your-secret-key-change-this-in-production
jwt.expiration=86400000

# Logging
logging.level.root=INFO
logging.level.com.vishnu.finance=DEBUG

# Swagger Documentation
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## Frontend Configuration

### API Base URL
Update in `src/services/api.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080/api/v1';
```

### Environment Variables (.env)
Create a `.env` file in the frontend root:
```
REACT_APP_API_URL=http://localhost:8080/api/v1
REACT_APP_ENV=development
```

## Database Setup

### Create Database
```bash
mysql -u root -p
CREATE DATABASE finance_db;
USE finance_db;
```

### Verify Tables (Auto-created by Hibernate)
```bash
SHOW TABLES;
DESC users;
DESC categories;
DESC transactions;
DESC budgets;
```

## CORS Configuration

The backend is configured to accept requests from:
- `http://localhost:3000` (React development server)
- `http://localhost:8080` (for testing)

To add more origins, update `config/SecurityConfig.java`:
```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:3000",
    "http://localhost:8080",
    "https://your-production-domain.com"
));
```

## JWT Token Configuration

### Generate Strong Secret Key
```bash
# Using OpenSSL (Linux/Mac)
openssl rand -base64 32

# Using PowerShell (Windows)
$bytes = New-Object Byte[] 32
([System.Security.Cryptography.RNGCryptoServiceProvider]::new()).GetBytes($bytes)
[Convert]::ToBase64String($bytes)
```

Update `application.properties`:
```properties
jwt.secret=your-generated-secret-key
jwt.expiration=86400000  # 24 hours in milliseconds
```

## Maven Dependencies

Key dependencies in `pom.xml`:
- Spring Boot 3.5.7
- Spring Security
- Spring Data JPA
- MySQL Connector Java 8.0.33
- Lombok
- JJWT (JWT)
- ModelMapper
- SpringDoc OpenAPI (Swagger)

## Build and Run

### Development
```bash
# Backend
mvn clean install
mvn spring-boot:run

# Frontend
npm install
npm start
```

### Production Build
```bash
# Backend
mvn clean package -DskipTests
java -jar target/finance-tracker-0.0.1-SNAPSHOT.jar

# Frontend
npm run build
# Deploy contents of build/ folder to static hosting
```

## Port Configuration

| Service    | Port | URL                  |
|-----------|------|----------------------|
| Backend   | 8080 | http://localhost:8080 |
| Frontend  | 3000 | http://localhost:3000 |
| MySQL     | 3306 | localhost:3306        |

## Security Checklist

- [ ] Change JWT secret key for production
- [ ] Update CORS allowed origins
- [ ] Set strong database password
- [ ] Enable HTTPS in production
- [ ] Configure firewall rules
- [ ] Set up backup strategy
- [ ] Enable logging and monitoring
- [ ] Implement rate limiting

## Troubleshooting

### Port Already in Use
```bash
# Windows - Find and kill process on port
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### MySQL Connection Issues
```bash
# Test connection
mysql -h localhost -u root -p

# Check MySQL status
mysql --version
```

### Node Dependencies Issues
```bash
# Clear npm cache
npm cache clean --force

# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install
```

## Development Tools

### Recommended IDE
- IntelliJ IDEA (Backend)
- VS Code (Frontend)

### Browser Extensions
- Redux DevTools
- React Developer Tools
- Postman (API Testing)

### API Testing
```bash
# Using curl
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Using Postman
# Import API requests from Postman collection
```

## Performance Optimization

### Backend
- Enable query logging for debugging
- Add database indexes for frequently queried columns
- Implement caching for dashboard data

### Frontend
- Use React.memo for component optimization
- Implement lazy loading for routes
- Minify CSS and JavaScript in production

## Monitoring and Logging

### Backend Logging
```properties
logging.level.root=INFO
logging.level.com.vishnu.finance=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Frontend Console Logging
```javascript
console.log('Debug info:', data);
console.error('Error occurred:', error);
```
