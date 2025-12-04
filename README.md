# Finance Tracker - Full Stack Application

A comprehensive finance management application built with Spring Boot and React. Track income, expenses, manage budgets, and visualize financial data with interactive dashboards.

## 🎯 Features

### Core Features
- **User Authentication**: Secure JWT-based authentication with signup and login
- **Transaction Management**: Create, read, update, delete transactions with categorization
- **Category Management**: Organize transactions by custom categories (Income/Expense)
- **Budget Planning**: Set monthly budgets for categories and track spending
- **Dashboard Analytics**: Visual representation of financial data with charts
- **Financial Reports**: Monthly income trends and expense breakdown by category

### Technical Features
- RESTful API with Spring Boot 3.5.7
- JWT token-based authentication
- MySQL database with JPA/Hibernate ORM
- CORS-enabled for cross-origin requests
- Global exception handling
- Input validation with custom DTOs
- React frontend with Tailwind CSS styling
- Chart.js integration for data visualization
- Responsive design for mobile and desktop

## 📋 Prerequisites

- **Backend**:
  - Java 17+
  - Maven 3.6+
  - MySQL 8.0+
  
- **Frontend**:
  - Node.js 16+ and npm 7+
  - React 18+

## 🚀 Installation & Setup

### Backend Setup

1. **Clone the repository**
   ```bash
   cd finance-tracker
   ```

2. **Configure MySQL Database**
   ```sql
   CREATE DATABASE finance_db;
   ```

3. **Update application.properties**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

4. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

   The frontend will start on `http://localhost:3000`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Authentication Endpoints

#### Signup
```http
POST /auth/signup
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "secure_password"
}
```

Response:
```json
{
  "message": "User registered successfully",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com"
  }
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "secure_password"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com"
  }
}
```

### Transaction Endpoints

#### Get All Transactions
```http
GET /transactions
Authorization: Bearer {token}
```

#### Create Transaction
```http
POST /transactions
Authorization: Bearer {token}
Content-Type: application/json

{
  "categoryId": 1,
  "amount": 50.00,
  "type": "EXPENSE",
  "note": "Grocery shopping",
  "date": "2025-12-04T10:30:00"
}
```

#### Get Transactions by Type
```http
GET /transactions/type/EXPENSE
Authorization: Bearer {token}
```

#### Get Total by Type
```http
GET /transactions/total/INCOME
Authorization: Bearer {token}
```

#### Update Transaction
```http
PUT /transactions/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 60.00,
  "note": "Updated grocery shopping"
}
```

#### Delete Transaction
```http
DELETE /transactions/{id}
Authorization: Bearer {token}
```

### Category Endpoints

#### Get All Categories
```http
GET /categories
Authorization: Bearer {token}
```

#### Create Category
```http
POST /categories
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Groceries",
  "description": "Food and household items",
  "type": "EXPENSE"
}
```

#### Update Category
```http
PUT /categories/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Updated Groceries",
  "description": "Food items only"
}
```

#### Delete Category
```http
DELETE /categories/{id}
Authorization: Bearer {token}
```

### Budget Endpoints

#### Get All Budgets
```http
GET /budgets
Authorization: Bearer {token}
```

#### Get Budgets by Month
```http
GET /budgets/month/2025-12
Authorization: Bearer {token}
```

#### Create Budget
```http
POST /budgets
Authorization: Bearer {token}
Content-Type: application/json

{
  "categoryId": 1,
  "budgetAmount": 300.00,
  "month": "2025-12"
}
```

#### Update Budget
```http
PUT /budgets/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "budgetAmount": 350.00
}
```

#### Delete Budget
```http
DELETE /budgets/{id}
Authorization: Bearer {token}
```

### Dashboard Endpoints

#### Get Dashboard Analytics
```http
GET /dashboard
Authorization: Bearer {token}
```

Response:
```json
{
  "totalIncome": 5000.00,
  "totalExpense": 1500.00,
  "netBalance": 3500.00,
  "categoryExpenses": {
    "Groceries": 450.00,
    "Entertainment": 300.00
  },
  "monthlyData": {
    "2025-01": 500.00,
    "2025-02": 600.00,
    ...
  },
  "transactionCount": 42
}
```

## 📁 Project Structure

### Backend
```
finance-tracker/
├── src/main/java/com/vishnu/finance/finance_tracker/
│   ├── FinanceTrackerApplication.java
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── TransactionController.java
│   │   ├── CategoryController.java
│   │   ├── BudgetController.java
│   │   └── AnalyticsController.java
│   ├── dto/
│   │   ├── UserDTO.java
│   │   ├── TransactionDTO.java
│   │   ├── CategoryDTO.java
│   │   ├── BudgetDTO.java
│   │   ├── AuthRequest.java
│   │   ├── AuthResponse.java
│   │   └── DashboardDTO.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Transaction.java
│   │   ├── Category.java
│   │   ├── Budget.java
│   │   └── TransactionType.java
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── TransactionRepository.java
│   │   ├── CategoryRepository.java
│   │   └── BudgetRepository.java
│   ├── security/
│   │   ├── JwtUtil.java
│   │   └── JwtAuthenticationFilter.java
│   └── service/
│       ├── UserService.java
│       ├── CustomUserDetailsService.java
│       ├── TransactionService.java
│       ├── CategoryService.java
│       ├── BudgetService.java
│       └── DashboardService.java
├── pom.xml
└── application.properties
```

### Frontend
```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── Navbar.js
│   │   └── Sidebar.js
│   ├── pages/
│   │   ├── Login.js
│   │   ├── Signup.js
│   │   ├── Dashboard.js
│   │   ├── Transactions.js
│   │   ├── Categories.js
│   │   └── Budgets.js
│   ├── services/
│   │   └── api.js
│   ├── App.js
│   ├── App.css
│   ├── index.js
│   └── index.css
├── tailwind.config.js
├── postcss.config.js
└── package.json
```

## 🔧 Configuration

### JWT Configuration
Update in `application.properties`:
```properties
jwt.secret=your-secret-key-change-this-in-production
jwt.expiration=86400000
```

### CORS Configuration
CORS is configured in `SecurityConfig.java` to allow requests from:
- `http://localhost:3000` (React dev server)
- `http://localhost:8080` (for testing)

Modify the `corsConfigurationSource()` method to add additional origins.

### Database Configuration
Update `application.properties` with your MySQL connection details:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=root
spring.datasource.password=your_password
```

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL
);
```

### Categories Table
```sql
CREATE TABLE categories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  type ENUM('INCOME', 'EXPENSE') NOT NULL,
  user_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Transactions Table
```sql
CREATE TABLE transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  type ENUM('INCOME', 'EXPENSE') NOT NULL,
  note VARCHAR(255),
  date DATETIME NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

### Budgets Table
```sql
CREATE TABLE budgets (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  budget_amount DECIMAL(10, 2) NOT NULL,
  month VARCHAR(7) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

## 🔐 Security Features

- **JWT Authentication**: Tokens are stored in localStorage on the client
- **Password Encryption**: Passwords are encrypted using BCrypt
- **CORS Protection**: Only whitelisted origins can access the API
- **Authorization**: Protected endpoints require valid JWT token
- **Input Validation**: All inputs are validated using annotations

## 🚀 Deployment

### Backend Deployment (AWS/Heroku)
1. Build JAR file: `mvn clean package`
2. Deploy JAR to your hosting platform
3. Set environment variables for database and JWT secret

### Frontend Deployment (Vercel/Netlify)
1. Build: `npm run build`
2. Deploy the `build/` directory to your hosting platform
3. Update API base URL in `src/services/api.js` to production API

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Kill process on port 8080 (Backend)
lsof -ti:8080 | xargs kill -9

# Kill process on port 3000 (Frontend)
lsof -ti:3000 | xargs kill -9
```

### Database Connection Error
- Ensure MySQL is running
- Verify connection credentials in `application.properties`
- Check database exists: `CREATE DATABASE finance_db;`

### CORS Error
- Verify frontend URL is whitelisted in `SecurityConfig.java`
- Clear browser cache and restart both servers

## 📝 Future Enhancements

- [ ] Recurring transactions
- [ ] Multiple currency support
- [ ] Export to PDF/Excel
- [ ] Mobile app (React Native)
- [ ] Advanced analytics and AI recommendations
- [ ] Multi-user accounts and sharing
- [ ] Two-factor authentication
- [ ] Data backup and restore

## 📄 License

This project is licensed under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Support

For support, email support@financetracker.com or open an issue on GitHub.

---

**Happy Tracking! 💰**
