# Deployment Guide

## Prerequisites
- Docker (optional, for containerization)
- AWS/GCP/Azure account (for cloud deployment)
- GitHub account for CI/CD
- Domain name (for production)

## Local Deployment Testing

### 1. Backend JAR Deployment
```bash
# Build JAR file
cd finance-tracker
mvn clean package -DskipTests

# Run JAR
java -jar target/finance-tracker-0.0.1-SNAPSHOT.jar
```

### 2. Frontend Build and Serve
```bash
cd frontend
npm run build

# Serve with simple HTTP server
npx serve -s build -l 3000
```

## Docker Deployment

### Backend Dockerfile
Create `Dockerfile` in project root:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/finance-tracker-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t finance-tracker-backend .
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/finance_db finance-tracker-backend
```

### Frontend Dockerfile
Create `frontend/Dockerfile`:
```dockerfile
FROM node:18 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

Build and run:
```bash
docker build -t finance-tracker-frontend ./frontend
docker run -p 3000:80 finance-tracker-frontend
```

### Docker Compose
Create `docker-compose.yml`:
```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: finance_db
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/finance_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root_password
      JWT_SECRET: your-secret-key
    depends_on:
      - mysql

  frontend:
    build: ./frontend
    ports:
      - "3000:80"
    depends_on:
      - backend

volumes:
  mysql-data:
```

Run all services:
```bash
docker-compose up
```

## Cloud Deployment

### AWS Deployment (EC2)

#### 1. Launch EC2 Instance
- Choose Ubuntu 20.04 LTS AMI
- Select t2.micro instance type
- Configure security groups (open ports 80, 443, 8080, 3306)
- Create key pair and download

#### 2. Connect to Instance
```bash
chmod 400 your-key.pem
ssh -i your-key.pem ubuntu@your-instance-ip
```

#### 3. Install Required Software
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk nodejs npm mysql-server

# Start MySQL
sudo service mysql start
sudo mysql -u root -e "CREATE DATABASE finance_db;"
```

#### 4. Deploy Backend
```bash
# Clone repository
git clone your-repo-url
cd finance-tracker

# Build
mvn clean package -DskipTests

# Run with screen (persistent)
screen -S backend
java -jar target/finance-tracker-0.0.1-SNAPSHOT.jar
# Press Ctrl+A then D to detach
```

#### 5. Deploy Frontend
```bash
cd frontend
npm install
npm run build

# Install and start Nginx
sudo apt install -y nginx
sudo cp -r build/* /var/www/html/
sudo service nginx start
```

### Heroku Deployment

#### Backend to Heroku
```bash
# Create Procfile
echo "web: java $JAVA_OPTS -Dserver.port=\$PORT -jar target/*.jar" > Procfile

# Create .slugignore
echo "frontend/" > .slugignore

# Deploy
heroku create your-app-name
heroku addons:create cleardb:ignite
git push heroku main
```

#### Frontend to Netlify
```bash
cd frontend
npm run build
# Drag and drop build/ folder to Netlify
```

## AWS RDS Database Setup

### Create RDS Instance
```bash
# Via AWS Console:
# 1. Create DB Instance
# 2. Choose MySQL
# 3. Create database "finance_db"
# 4. Note endpoint and credentials
```

### Update Backend Configuration
```properties
spring.datasource.url=jdbc:mysql://your-rds-endpoint:3306/finance_db
spring.datasource.username=admin
spring.datasource.password=your-password
```

## Environment Variables for Production

### Backend
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/finance_db
SPRING_DATASOURCE_USERNAME=prod_user
SPRING_DATASOURCE_PASSWORD=strong_password
JWT_SECRET=production-secret-key-minimum-32-chars
SERVER_PORT=8080
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

### Frontend
```bash
REACT_APP_API_URL=https://api.yourdomian.com
REACT_APP_ENV=production
```

## SSL/HTTPS Setup

### Using Let's Encrypt with Nginx
```bash
sudo apt install -y certbot python3-certbot-nginx
sudo certbot certonly --nginx -d yourdomain.com
sudo certbot renew --dry-run
```

### Update Nginx Configuration
```nginx
server {
    listen 443 ssl;
    server_name yourdomain.com;

    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;

    location / {
        root /var/www/html;
        try_files $uri /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080/api;
    }
}

server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$server_name$request_uri;
}
```

## CI/CD Pipeline (GitHub Actions)

Create `.github/workflows/deploy.yml`:
```yaml
name: Deploy

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v2

      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'adopt'

      - name: Build backend
        run: mvn clean package -DskipTests

      - name: Build frontend
        run: |
          cd frontend
          npm install
          npm run build

      - name: Deploy to server
        env:
          DEPLOY_KEY: ${{ secrets.DEPLOY_KEY }}
          DEPLOY_HOST: ${{ secrets.DEPLOY_HOST }}
        run: |
          # Add deployment script
          bash scripts/deploy.sh
```

## Monitoring and Maintenance

### Application Monitoring
```bash
# Monitor logs
tail -f catalina.out

# Check resource usage
top
df -h
free -m
```

### Database Backup
```bash
# Backup
mysqldump -u root -p finance_db > backup-$(date +%Y%m%d).sql

# Restore
mysql -u root -p finance_db < backup-20250101.sql
```

### Log Rotation
```bash
# Configure logrotate
sudo nano /etc/logrotate.d/finance-tracker

/var/log/finance-tracker/*.log {
    daily
    missingok
    rotate 7
    compress
    delaycompress
}
```

## Production Checklist

- [ ] Change JWT secret key
- [ ] Update CORS allowed origins
- [ ] Configure HTTPS/SSL
- [ ] Enable database encryption
- [ ] Set up automated backups
- [ ] Configure logging and monitoring
- [ ] Set up alerting for errors
- [ ] Implement rate limiting
- [ ] Enable request/response compression
- [ ] Configure CDN for static assets
- [ ] Set up firewall rules
- [ ] Enable database connection pooling
- [ ] Configure connection timeouts
- [ ] Test disaster recovery
- [ ] Document deployment process

## Rollback Procedure

```bash
# Keep previous versions
mv target/finance-tracker-0.0.1-SNAPSHOT.jar target/finance-tracker-backup.jar

# Kill running instance
pkill -f "java.*finance-tracker"

# Restore previous version
java -jar target/finance-tracker-backup.jar
```

## Performance Optimization

### Database
```sql
-- Add indexes
CREATE INDEX idx_user_id ON transactions(user_id);
CREATE INDEX idx_category_id ON transactions(category_id);
CREATE INDEX idx_date ON transactions(date);
CREATE INDEX idx_user_category ON budgets(user_id, category_id);
```

### Application
```properties
# Connection pooling
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5

# Cache configuration
spring.cache.type=simple
```

## Troubleshooting Production Issues

### High Memory Usage
```bash
# Check Java processes
jps -l
# Increase heap
java -Xmx1024m -Xms512m -jar app.jar
```

### Database Connection Timeout
```properties
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000
```

### Slow Queries
Enable query logging:
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

**Deployment Complete!** 🚀
