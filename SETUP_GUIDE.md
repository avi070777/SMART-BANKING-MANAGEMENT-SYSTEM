# 📘 Complete Setup Guide - Smart Banking System

## Table of Contents
1. [System Requirements](#system-requirements)
2. [Installing Java](#installing-java)
3. [Installing MySQL](#installing-mysql)
4. [Setting Up Database](#setting-up-database)
5. [Configuring the Application](#configuring-the-application)
6. [Running the Application](#running-the-application)
7. [IDE Setup](#ide-setup)
8. [Troubleshooting](#troubleshooting)

---

## System Requirements

### Minimum Requirements:
- **OS**: Windows 10, macOS 10.14+, or Linux (Ubuntu 20.04+)
- **RAM**: 2GB
- **Disk Space**: 500MB free
- **Internet**: For downloading software

### Software Needed:
- Java JDK 11 or higher
- MySQL Server 8.0 or higher
- MySQL JDBC Driver (Connector/J)
- Text Editor or IDE (VS Code, Eclipse, IntelliJ IDEA)

---

## Installing Java

### Windows

1. **Download JDK**
   - Visit: https://www.oracle.com/java/technologies/downloads/
   - Download "Windows x64 Installer"

2. **Install JDK**
   - Run the downloaded `.exe` file
   - Follow installation wizard
   - Note installation path (e.g., `C:\Program Files\Java\jdk-17`)

3. **Set Environment Variables**
   ```
   # Add to System Variables:
   JAVA_HOME = C:\Program Files\Java\jdk-17
   
   # Add to Path:
   %JAVA_HOME%\bin
   ```

4. **Verify Installation**
   ```cmd
   java -version
   javac -version
   ```

### macOS

1. **Using Homebrew** (Recommended)
   ```bash
   # Install Homebrew if not installed
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   
   # Install Java
   brew install openjdk@17
   
   # Link Java
   sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
   ```

2. **Verify Installation**
   ```bash
   java -version
   javac -version
   ```

### Linux (Ubuntu/Debian)

```bash
# Update package list
sudo apt update

# Install OpenJDK
sudo apt install openjdk-17-jdk -y

# Verify installation
java -version
javac -version
```

---

## Installing MySQL

### Windows

1. **Download MySQL Installer**
   - Visit: https://dev.mysql.com/downloads/installer/
   - Download "MySQL Installer for Windows"

2. **Run Installer**
   - Choose "Developer Default" setup type
   - Click "Execute" to download and install components

3. **Configure MySQL Server**
   - Type and Networking: Use default (Port 3306)
   - Authentication Method: Use strong password encryption
   - Accounts and Roles:
     - Root Password: **Set a strong password and remember it!**
     - (Optional) Create additional user account

4. **Complete Installation**
   - Apply Configuration
   - Click "Finish"

5. **Verify Installation**
   ```cmd
   # Open Command Prompt
   mysql --version
   
   # Login to MySQL
   mysql -u root -p
   # Enter your password
   ```

### macOS

1. **Using Homebrew** (Easiest)
   ```bash
   # Install MySQL
   brew install mysql
   
   # Start MySQL service
   brew services start mysql
   
   # Secure installation (set root password)
   mysql_secure_installation
   ```

2. **Follow Prompts:**
   - Validate Password: No (or Yes for production)
   - Set root password: **Enter a strong password**
   - Remove anonymous users: Yes
   - Disallow root login remotely: Yes
   - Remove test database: Yes
   - Reload privilege tables: Yes

3. **Verify Installation**
   ```bash
   mysql --version
   mysql -u root -p
   ```

### Linux (Ubuntu/Debian)

```bash
# Update package list
sudo apt update

# Install MySQL Server
sudo apt install mysql-server -y

# Start MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql

# Secure installation
sudo mysql_secure_installation

# Verify installation
mysql --version
sudo mysql -u root -p
```

---

## Setting Up Database

### Step 1: Download MySQL JDBC Driver

1. Visit: https://dev.mysql.com/downloads/connector/j/
2. Select "Platform Independent" from dropdown
3. Download the ZIP archive
4. Extract the ZIP file
5. Find `mysql-connector-java-8.x.xx.jar` (or similar)
6. Copy this JAR file to your project directory

### Step 2: Create Database

1. **Open MySQL Command Line**

   **Windows:**
   ```cmd
   mysql -u root -p
   ```

   **macOS/Linux:**
   ```bash
   mysql -u root -p
   ```
   
   Enter your root password when prompted.

2. **Run Database Setup Script**

   **Option A: Copy-Paste Method**
   - Open `database_setup.sql` in a text editor
   - Copy all content
   - Paste into MySQL command line
   - Press Enter

   **Option B: Source Command**
   ```sql
   -- Inside MySQL shell
   source /full/path/to/database_setup.sql;
   ```

   **Option C: Command Line Direct**
   ```bash
   # Outside MySQL shell
   mysql -u root -p < database_setup.sql
   ```

3. **Verify Database Creation**
   ```sql
   -- Inside MySQL
   SHOW DATABASES;
   USE smart_banking;
   SHOW TABLES;
   
   -- Check if users were created
   SELECT account_number, full_name, email, role FROM users;
   
   -- You should see 4 accounts (1 admin, 3 users)
   ```

---

## Configuring the Application

### Step 1: Edit BankingSystem.java

1. Open `BankingSystem.java` in any text editor

2. Find these lines (around line 20-22):
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_banking";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_password";
   ```

3. Update the password:
   ```java
   private static final String DB_PASSWORD = "your_actual_mysql_password";
   ```

4. **Optional**: If you created a different MySQL user:
   ```java
   private static final String DB_USER = "your_mysql_username";
   ```

5. **Optional**: If MySQL is on different port:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:YOUR_PORT/smart_banking";
   ```

6. Save the file

---

## Running the Application

### Method 1: Command Line (All Platforms)

1. **Open Terminal/Command Prompt**
   - Windows: Press `Win + R`, type `cmd`, press Enter
   - macOS: Press `Cmd + Space`, type "Terminal"
   - Linux: Press `Ctrl + Alt + T`

2. **Navigate to Project Directory**
   ```bash
   cd /path/to/smart-banking-system
   ```

3. **Compile the Program**
   
   **Windows:**
   ```cmd
   javac -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem.java
   ```
   
   **macOS/Linux:**
   ```bash
   javac -cp ".:mysql-connector-java-8.x.xx.jar" BankingSystem.java
   ```
   
   **Note**: Replace `mysql-connector-java-8.x.xx.jar` with your actual JAR filename

4. **Run the Program**
   
   **Windows:**
   ```cmd
   java -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem
   ```
   
   **macOS/Linux:**
   ```bash
   java -cp ".:mysql-connector-java-8.x.xx.jar" BankingSystem
   ```

### Method 2: Using Eclipse IDE

1. **Create New Java Project**
   - File → New → Java Project
   - Project name: `SmartBankingSystem`
   - Click "Finish"

2. **Add MySQL JDBC Library**
   - Right-click project → Build Path → Configure Build Path
   - Libraries tab → Add External JARs
   - Select `mysql-connector-java-8.x.xx.jar`
   - Click "Apply and Close"

3. **Add Source File**
   - Right-click `src` folder → New → Class
   - Name: `BankingSystem`
   - Delete the generated code
   - Copy-paste content from `BankingSystem.java`
   - Save file

4. **Run Application**
   - Right-click `BankingSystem.java` → Run As → Java Application

### Method 3: Using IntelliJ IDEA

1. **Create New Project**
   - File → New → Project
   - Java
   - Project name: `SmartBankingSystem`
   - Click "Create"

2. **Add MySQL JDBC Library**
   - File → Project Structure → Libraries
   - Click "+" → Java
   - Select `mysql-connector-java-8.x.xx.jar`
   - Click "OK"

3. **Add Source File**
   - Right-click `src` folder → New → Java Class
   - Name: `BankingSystem`
   - Copy-paste content from `BankingSystem.java`
   - Save file

4. **Run Application**
   - Right-click `BankingSystem.java` → Run 'BankingSystem.main()'

### Method 4: Using VS Code

1. **Install Java Extension Pack**
   - Open VS Code
   - Go to Extensions (Ctrl+Shift+X)
   - Search "Java Extension Pack"
   - Install it

2. **Open Project Folder**
   - File → Open Folder
   - Select your project directory

3. **Configure Classpath**
   - Create `.vscode/settings.json`:
   ```json
   {
       "java.project.referencedLibraries": [
           "lib/**/*.jar",
           "mysql-connector-java-8.x.xx.jar"
       ]
   }
   ```

4. **Run Application**
   - Open `BankingSystem.java`
   - Press F5 or click "Run" above main method

---

## Testing the Application

### 1. First Login

```
Account Number: SB000002
Password: test123
```

### 2. Try Each Feature

1. **View Account Details** - Check your balance
2. **Deposit Money** - Add ₹5000
3. **View Transaction History** - See the deposit
4. **Transfer Funds** 
   - Recipient: SB000003
   - Amount: ₹2000
5. **Check Balance** - Verify deduction

### 3. Test Admin Account

```
Logout and login as:
Account Number: SB000001
Password: admin123

Then select: Admin Panel
```

---

## Troubleshooting

### Issue 1: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Cause**: JDBC driver not in classpath

**Solution**:
- Verify JAR file is in project directory
- Check compile/run commands include the JAR
- In IDE, ensure library is added to build path

### Issue 2: "Access denied for user 'root'@'localhost'"

**Cause**: Wrong MySQL password in code

**Solution**:
```java
// Update this line in BankingSystem.java:
private static final String DB_PASSWORD = "correct_password_here";
```

### Issue 3: "Unknown database 'smart_banking'"

**Cause**: Database not created

**Solution**:
```bash
mysql -u root -p < database_setup.sql
```

### Issue 4: "Communications link failure"

**Cause**: MySQL server not running

**Solution**:
```bash
# Windows
net start MySQL80

# macOS
brew services start mysql

# Linux
sudo systemctl start mysql
```

### Issue 5: Compilation Errors

**Cause**: Java version mismatch or syntax errors

**Solution**:
```bash
# Check Java version (need 11+)
java -version
javac -version

# If version is old, update Java
```

### Issue 6: "Port 3306 is already in use"

**Cause**: Another MySQL instance or process using port

**Solution**:
```bash
# Windows - Check what's using port 3306
netstat -ano | findstr :3306

# macOS/Linux
lsof -i :3306

# Change port in MySQL config and update DB_URL in code
```

---

## Additional Tips

### Creating a Batch/Shell Script to Run

**Windows (run.bat):**
```batch
@echo off
javac -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem.java
java -cp ".;mysql-connector-java-8.x.xx.jar" BankingSystem
pause
```

**macOS/Linux (run.sh):**
```bash
#!/bin/bash
javac -cp ".:mysql-connector-java-8.x.xx.jar" BankingSystem.java
java -cp ".:mysql-connector-java-8.x.xx.jar" BankingSystem
```

Make it executable:
```bash
chmod +x run.sh
./run.sh
```

---

## Need Help?

1. **Review error messages carefully** - They often tell you exactly what's wrong
2. **Check MySQL is running** - `mysql -u root -p`
3. **Verify credentials** - Database password in code matches MySQL
4. **Test JDBC connection separately** - Create simple test program
5. **Contact**: avijaiswal346@gmail.com

---

**Next Steps**: After successful setup, try the [Usage Guide](README.md#usage-guide) to explore all features!

Happy Banking! 💰