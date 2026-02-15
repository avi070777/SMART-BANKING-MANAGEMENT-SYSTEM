# ⚡ Quick Reference - Smart Banking System

## 🎯 Get Started in 5 Minutes

### 1. Install MySQL (2 min)
```bash
# Windows: Download from https://dev.mysql.com/downloads/installer/
# macOS: brew install mysql && brew services start mysql
# Linux: sudo apt install mysql-server && sudo systemctl start mysql
```

### 2. Setup Database (1 min)
```bash
mysql -u root -p < database_setup.sql
```

### 3. Download JDBC Driver (30 sec)
- https://dev.mysql.com/downloads/connector/j/
- Extract ZIP → Copy `.jar` to project folder

### 4. Configure & Run (1.5 min)
```java
// Edit BankingSystem.java line 22:
private static final String DB_PASSWORD = "your_mysql_password";
```

```bash
# Compile (Windows):
javac -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem.java

# Run (Windows):
java -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem

# macOS/Linux: Use : instead of ;
javac -cp ".:mysql-connector-java-8.x.xx.jar" BankingSystem.java
java -cp ".:mysql-connector-java-8.x.xx.jar" BankingSystem
```

---

## 🔑 Test Accounts

| Account | Password | Role | Balance |
|---------|----------|------|---------|
| SB000001 | admin123 | ADMIN | ₹10,00,000 |
| SB000002 | test123 | USER | ₹50,000 |
| SB000003 | test123 | USER | ₹75,000 |
| SB000004 | test123 | USER | ₹1,00,000 |

---

## 📋 Main Menu Options

```
1. View Account Details    → Check balance, account info
2. Deposit Money          → Add money to account
3. Withdraw Money         → Remove money (min ₹1000 balance)
4. Transfer Funds         → Send money to another account
5. View Transaction History → Last 10 transactions
6. Change Password        → Update your password
7. Admin Panel           → View all users (ADMIN only)
0. Logout                → Exit to login screen
```

---

## 💡 Quick Commands

### MySQL Commands
```sql
-- Login
mysql -u root -p

-- Check database
SHOW DATABASES;
USE smart_banking;
SHOW TABLES;

-- View users
SELECT * FROM users;

-- View transactions
SELECT * FROM transactions ORDER BY transaction_date DESC LIMIT 10;

-- Reset password for user (if forgot)
UPDATE users SET password = 'ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae' 
WHERE account_number = 'SB000002';
-- This sets password to: test123

-- Check total balance in system
SELECT SUM(balance) as total_balance FROM users;
```

### Java Compilation
```bash
# Windows
javac -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem.java
java -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem

# macOS/Linux
javac -cp ".:mysql-connector-java-8.x.xx.jar" BankingSystem.java
java -cp ".:mysql-connector-java-8.x.xx.jar" BankingSystem
```

---

## 🐛 Common Issues - Quick Fixes

| Problem | Solution |
|---------|----------|
| **ClassNotFoundException** | Add JDBC JAR to classpath |
| **Access denied** | Fix password in line 22 of code |
| **Database not found** | Run database_setup.sql |
| **Connection failed** | Start MySQL: `net start MySQL80` (Win) or `brew services start mysql` (Mac) |
| **Port in use** | Change port or stop other MySQL instance |

---

## 🎓 Testing Workflow

```
1. Login as SB000002 (test123)
2. View Account Details → See ₹50,000 balance
3. Deposit ₹5000 → Balance: ₹55,000
4. Transfer ₹2000 to SB000003
5. Check Transaction History
6. Logout
7. Login as SB000003 (test123)
8. Check balance → Should be ₹77,000 (75k + 2k)
9. View Transaction History → See received transfer
```

---

## 🔐 Security Features

- ✅ SHA-256 Password Hashing
- ✅ ACID Transaction Compliance
- ✅ SQL Injection Prevention (Prepared Statements)
- ✅ Role-Based Access Control
- ✅ Minimum Balance Enforcement (₹1000)
- ✅ Transaction Rollback on Failure

---

## 📁 Project Files

```
smart-banking-system/
├── BankingSystem.java          # Main application
├── database_setup.sql          # Database schema
├── README.md                   # Full documentation
├── SETUP_GUIDE.md              # Detailed setup
├── QUICK_REFERENCE.md          # This file
└── mysql-connector-java.jar    # JDBC driver
```

---

## 📤 GitHub Upload - 3 Methods

### Method 1: Command Line
```bash
git init
git add .
git commit -m "Initial commit: Smart Banking System"
git remote add origin https://github.com/YOUR_USERNAME/smart-banking-system.git
git branch -M main
git push -u origin main
```

### Method 2: GitHub Desktop
1. Open GitHub Desktop
2. File → New Repository → Name: smart-banking-system
3. Copy files to folder
4. Commit → Publish

### Method 3: Website Upload
1. github.com → New repository
2. Upload files directly
3. Commit

**Don't forget .gitignore:**
```
*.class
*.jar
.idea/
.DS_Store
```

---

## 🎯 Key Features to Mention

✅ User Account Management  
✅ Secure Fund Transfers (ACID)  
✅ Real-time Transaction Tracking  
✅ Role-Based Security  
✅ Password Encryption (SHA-256)  
✅ JDBC MySQL Integration  
✅ OOP Design Principles  

---

## 📞 Support

**Email**: avijaiswal346@gmail.com  
**LinkedIn**: [linkedin.com/in/avi0707](https://linkedin.com/in/avi0707)  
**Phone**: +91-7007646461

---

## 🏆 Resume Points

- Engineered robust Java banking application using OOP principles
- Integrated MySQL via JDBC for real-time transaction tracking
- Implemented ACID-compliant fund transfers with rollback support
- Developed role-based security with SHA-256 password encryption
- Performed rigorous unit testing ensuring data integrity

---

**Made with ❤️ by Avi Jaiswal**