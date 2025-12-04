# Quick Start Guide

## Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0+

## Setup Steps

### 1. Backend Setup (5 minutes)

```bash
cd finance-tracker

# Create MySQL Database
mysql -u root -p
CREATE DATABASE finance_db;
EXIT;

# Configure database connection in src/main/resources/application.properties
# Update: spring.datasource.password=your_password

# Build and Run
mvn clean install
mvn spring-boot:run
```

✅ Backend running on http://localhost:8080

### 2. Frontend Setup (5 minutes)

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

✅ Frontend running on http://localhost:3000

### 3. Access the Application

1. Open http://localhost:3000 in your browser
2. Click "Sign Up" to create a new account
3. Enter your credentials and click "Sign Up"
4. Login with your credentials
5. Start managing your finances!

## Basic Workflow

### 1. Create Categories
- Navigate to "Categories" section
- Click "Add Category"
- Create categories like "Groceries", "Entertainment", "Salary" etc.

### 2. Add Transactions
- Go to "Transactions" section
- Click "Add Transaction"
- Select category, amount, type (Income/Expense), and date
- Click "Add Transaction"

### 3. Set Budgets
- Navigate to "Budgets" section
- Click "Set Budget"
- Select a category and set monthly budget amount
- Monitor your spending against the budget

### 4. View Dashboard
- Open "Dashboard" to see:
  - Total Income and Expense
  - Net Balance
  - Expenses by category (Pie chart)
  - Monthly income trend (Bar chart)
  - Transaction count

## Default Test Account (After First Signup)
- Create your own account by signing up

## Troubleshooting

### Backend won't start
```bash
# Check if MySQL is running
# Update database credentials in application.properties
# Check if port 8080 is available
```

### Frontend shows blank page
```bash
# Clear browser cache (Ctrl+Shift+Delete)
# Check console for errors (F12)
# Ensure backend is running
```

### Database connection error
```bash
# Verify MySQL service is running
mysql -u root -p
SHOW DATABASES;
```

## API Documentation
Full API documentation available in main README.md

## Support
For issues, check the main README.md or contact support
