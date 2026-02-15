# 💰 Smart Banking Management System

A robust Java-based console application for managing banking operations with MySQL database integration, featuring secure fund transfers, transaction tracking, and role-based access control.

![Java](https://img.shields.io/badge/Java-11+-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)
![JDBC](https://img.shields.io/badge/JDBC-Enabled-green)
![Security](https://img.shields.io/badge/Security-SHA--256-red)

## 🎯 Features

### Core Banking Operations
- ✅ **User Account Management**: Create and manage bank accounts
- ✅ **Secure Authentication**: SHA-256 password hashing
- ✅ **Fund Transfers**: Transfer money between accounts with ACID compliance
- ✅ **Deposit & Withdrawal**: Manage account balance
- ✅ **Transaction History**: Real-time tracking of all operations
- ✅ **Role-Based Security**: USER and ADMIN roles with different access levels

### Technical Highlights
- **OOP Principles**: Clean object-oriented design
- **JDBC Integration**: Direct MySQL database connectivity
- **ACID Compliance**: Transaction integrity with rollback support
- **Data Security**: Password encryption using SHA-256
- **Input Validation**: Robust error handling and data validation
- **Minimum Balance**: Enforces ₹1000 minimum balance rule

## 📋 Prerequisites

### Required Software
1. **Java Development Kit (JDK) 11 or higher**
   - Download: https://www.oracle.com/java/technologies/downloads/
   
2. **MySQL Server 8.0 or higher**
   - Download: https://dev.mysql.com/downloads/mysql/
   
3. **MySQL JDBC Driver (Connector/J)**
   - Download: https://dev.mysql.com/downloads/connector/j/

## 🚀 Quick Start (5 Minutes!)

### Step 1: Install MySQL (2 minutes)
```bash
# Windows: Download and run installer
# https://dev.mysql.com/downloads/installer/

# macOS (using Homebrew):
brew install mysql
brew services start mysql

# Linux (Ubuntu/Debian):
sudo apt update && sudo apt install mysql-server
sudo systemctl start mysql
```

### Step 2: Setup Database (1 minute)
```bash
# Login to MySQL
mysql -u root -p

# Copy and paste database_setup.sql content
# Or run: source /path/to/database_setup.sql
```

### Step 3: Download JDBC Driver (30 seconds)
- Visit: https://dev.mysql.com/downloads/connector/j/
- Download ZIP → Extract → Copy `.jar` file to project folder

### Step 4: Configure & Run (1.5 minutes)
1. Open `BankingSystem.java`
2. Update line 22: `DB_PASSWORD = "your_mysql_password"`
3. Compile and run:

```bash
# Compile
javac -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem.java

# Run
java -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem
```

**Note**: On macOS/Linux, use `:` instead of `;` in classpath

## 🔐 Test Accounts

### Admin Account
- **Account**: SB000001 | **Password**: admin123 | **Balance**: ₹10,00,000

### User Accounts
- **Account**: SB000002 | **Password**: test123 | **Balance**: ₹50,000 (John Doe)
- **Account**: SB000003 | **Password**: test123 | **Balance**: ₹75,000 (Jane Smith)
- **Account**: SB000004 | **Password**: test123 | **Balance**: ₹1,00,000 (Robert Johnson)

## 📖 Usage Guide

### Main Features:

1. **Login**: Use test accounts above
2. **View Account**: Check balance and details
3. **Deposit**: Add money to account
4. **Withdraw**: Remove money (min balance ₹1000)
5. **Transfer**: Send money to other accounts (ACID-compliant)
6. **History**: View last 10 transactions
7. **Admin Panel**: View all users (admin only)

## 🏗️ Project Structure

```
smart-banking-system/
├── BankingSystem.java          # Main application
├── database_setup.sql          # Database schema
├── README.md                   # Documentation
├── SETUP_GUIDE.md             # Detailed setup
└── mysql-connector-java.jar   # JDBC driver
```

## 🔧 Database Schema

### Tables:
- **users**: Account information, balances, roles
- **transactions**: Complete transaction history

### Key Features:
- Foreign key relationships
- Indexes for performance
- Stored procedures for transfers
- Triggers for login tracking
- View for account summaries

## 🛡️ Security Features

1. **Password Security**: SHA-256 hashing
2. **ACID Compliance**: Transaction rollback on failure
3. **SQL Injection Prevention**: Prepared statements
4. **Role-Based Access**: USER vs ADMIN permissions
5. **Minimum Balance**: Prevents overdrafts

## 🐛 Troubleshooting

### "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
✅ **Solution**: Add JDBC JAR to classpath in compile/run commands

### "Access denied for user 'root'"
✅ **Solution**: Update `DB_PASSWORD` in code with your MySQL password

### "Communications link failure"
✅ **Solution**: Start MySQL service:
```bash
# Windows: net start MySQL80
# macOS: brew services start mysql
# Linux: sudo systemctl start mysql
```

### "Database 'smart_banking' doesn't exist"
✅ **Solution**: Run `database_setup.sql` script first

## 📤 Upload to GitHub

### Method 1: Command Line
```bash
# Initialize
git init
git add .
git commit -m "Initial commit: Smart Banking System"

# Create repo on GitHub, then:
git remote add origin https://github.com/YOUR_USERNAME/smart-banking-system.git
git branch -M main
git push -u origin main
```

### Method 2: GitHub Desktop
1. Download GitHub Desktop
2. File → New Repository
3. Copy files to folder
4. Commit and publish

### Method 3: GitHub Website
1. Create new repository
2. Upload files directly
3. Commit changes

**Important**: Create `.gitignore` and don't commit:
```
*.class
*.jar
.idea/
.vscode/
.DS_Store
```

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ Core Java & OOP concepts
- ✅ JDBC database connectivity
- ✅ MySQL database design
- ✅ Transaction management (ACID)
- ✅ Security best practices
- ✅ Error handling & validation
- ✅ SDLC implementation

## 🔄 Future Enhancements

- [ ] GUI using JavaFX or Swing
- [ ] Web interface with JSP/Servlets
- [ ] Email notifications
- [ ] PDF statement generation
- [ ] Multi-currency support
- [ ] Loan management
- [ ] Interest calculation
- [ ] Mobile app integration

## 👤 Author

**Avi Jaiswal**
- B.Tech Computer Science Engineering
- Galgotias College of Engineering and Technology, Greater Noida
- Email: avijaiswal346@gmail.com
- LinkedIn: [linkedin.com/in/avi0707](https://linkedin.com/in/avi0707)
- Phone: +91-7007646461

## 🙏 Acknowledgments

- GeeksForGeeks for internship experience
- Galgotias College of Engineering and Technology
- Oracle for Java and MySQL

---

⭐ **Star this repository if you found it helpful!**

📧 **Questions?** Email: avijaiswal346@gmail.com

Made with ❤️ using Java, JDBC, and MySQL