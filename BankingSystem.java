import java.sql.*;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Smart Banking Management System
 * Features: User Account Management, Fund Transfers, Transaction History, Role-Based Security
 * Author: Avi Jaiswal
 * Technologies: Java, JDBC, MySQL, OOP
 */

public class BankingSystem {
    
    // Database connection configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_banking";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "your_password";
    
    private static Connection connection = null;
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║     SMART BANKING MANAGEMENT SYSTEM v1.0             ║");
        System.out.println("║     Secure • Fast • Reliable                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        try {
            // Initialize database connection
            initializeDatabase();
            
            boolean running = true;
            while (running) {
                if (currentUser == null) {
                    showLoginMenu();
                } else {
                    showMainMenu();
                }
            }
            
        } catch (Exception e) {
            System.err.println("System Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
    
    // Initialize database connection
    private static void initializeDatabase() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Database connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Please add it to classpath.");
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("Database connection failed. Please check credentials and ensure MySQL is running.");
            throw e;
        }
    }
    
    // Show login/registration menu
    private static void showLoginMenu() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║         WELCOME TO SMART BANK        ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("1. Login");
        System.out.println("2. Create New Account");
        System.out.println("3. Exit");
        System.out.print("\nEnter your choice: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    System.out.println("\nThank you for using Smart Bank. Goodbye!");
                    closeConnection();
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            scanner.nextLine(); // Clear buffer
        }
    }
    
    // User login
    private static void login() {
        System.out.println("\n═══════════════ LOGIN ═══════════════");
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        try {
            String hashedPassword = hashPassword(password);
            String query = "SELECT * FROM users WHERE account_number = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, accountNumber);
            stmt.setString(2, hashedPassword);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                currentUser = new User(
                    rs.getInt("user_id"),
                    rs.getString("account_number"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getDouble("balance"),
                    rs.getString("role")
                );
                
                System.out.println("\n✓ Login successful! Welcome, " + currentUser.getFullName());
                logTransaction(currentUser.getUserId(), "LOGIN", 0, "User logged in");
            } else {
                System.out.println("❌ Invalid account number or password.");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Login failed: " + e.getMessage());
        }
    }
    
    // Create new account
    private static void createAccount() {
        System.out.println("\n═══════════════ CREATE NEW ACCOUNT ═══════════════");
        
        try {
            System.out.print("Full Name: ");
            String fullName = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Phone Number: ");
            String phone = scanner.nextLine();
            
            System.out.print("Initial Deposit (minimum ₹1000): ₹");
            double initialDeposit = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            
            if (initialDeposit < 1000) {
                System.out.println("❌ Minimum initial deposit is ₹1000.");
                return;
            }
            
            System.out.print("Create Password: ");
            String password = scanner.nextLine();
            
            System.out.print("Confirm Password: ");
            String confirmPassword = scanner.nextLine();
            
            if (!password.equals(confirmPassword)) {
                System.out.println("❌ Passwords do not match.");
                return;
            }
            
            // Generate unique account number
            String accountNumber = generateAccountNumber();
            String hashedPassword = hashPassword(password);
            
            // Insert into database
            String query = "INSERT INTO users (account_number, full_name, email, phone, password, balance, role) VALUES (?, ?, ?, ?, ?, ?, 'USER')";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, accountNumber);
            stmt.setString(2, fullName);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, hashedPassword);
            stmt.setDouble(6, initialDeposit);
            
            int rowsAffected = stmt.executeQuery();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    logTransaction(userId, "ACCOUNT_CREATED", initialDeposit, "New account created with initial deposit");
                }
                
                System.out.println("\n✓ Account created successfully!");
                System.out.println("═══════════════════════════════════════");
                System.out.println("Your Account Number: " + accountNumber);
                System.out.println("Initial Balance: ₹" + initialDeposit);
                System.out.println("═══════════════════════════════════════");
                System.out.println("Please save your account number for future login.");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Account creation failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Please try again.");
            scanner.nextLine(); // Clear buffer
        }
    }
    
    // Show main menu
    private static void showMainMenu() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              SMART BANKING DASHBOARD                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("Account: " + currentUser.getAccountNumber() + " | Balance: ₹" + String.format("%.2f", currentUser.getBalance()));
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("1. View Account Details");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Transfer Funds");
        System.out.println("5. View Transaction History");
        System.out.println("6. Change Password");
        
        if ("ADMIN".equals(currentUser.getRole())) {
            System.out.println("7. Admin Panel (View All Users)");
        }
        
        System.out.println("0. Logout");
        System.out.print("\nEnter your choice: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    viewAccountDetails();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    withdrawMoney();
                    break;
                case 4:
                    transferFunds();
                    break;
                case 5:
                    viewTransactionHistory();
                    break;
                case 6:
                    changePassword();
                    break;
                case 7:
                    if ("ADMIN".equals(currentUser.getRole())) {
                        adminPanel();
                    } else {
                        System.out.println("❌ Unauthorized access.");
                    }
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            scanner.nextLine(); // Clear buffer
        }
    }
    
    // View account details
    private static void viewAccountDetails() {
        System.out.println("\n═══════════════ ACCOUNT DETAILS ═══════════════");
        System.out.println("Account Number: " + currentUser.getAccountNumber());
        System.out.println("Name: " + currentUser.getFullName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Current Balance: ₹" + String.format("%.2f", currentUser.getBalance()));
        System.out.println("Account Type: " + currentUser.getRole());
        System.out.println("═══════════════════════════════════════════════");
        
        pressEnterToContinue();
    }
    
    // Deposit money
    private static void depositMoney() {
        System.out.println("\n═══════════════ DEPOSIT MONEY ═══════════════");
        System.out.print("Enter amount to deposit: ₹");
        
        try {
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            
            if (amount <= 0) {
                System.out.println("❌ Invalid amount. Please enter a positive value.");
                return;
            }
            
            // Update balance in database
            String query = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setDouble(1, amount);
            stmt.setInt(2, currentUser.getUserId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                currentUser.setBalance(currentUser.getBalance() + amount);
                logTransaction(currentUser.getUserId(), "DEPOSIT", amount, "Cash deposit");
                
                System.out.println("✓ Deposit successful!");
                System.out.println("Amount Deposited: ₹" + String.format("%.2f", amount));
                System.out.println("New Balance: ₹" + String.format("%.2f", currentUser.getBalance()));
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Deposit failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Please enter a valid amount.");
            scanner.nextLine(); // Clear buffer
        }
        
        pressEnterToContinue();
    }
    
    // Withdraw money
    private static void withdrawMoney() {
        System.out.println("\n═══════════════ WITHDRAW MONEY ═══════════════");
        System.out.println("Available Balance: ₹" + String.format("%.2f", currentUser.getBalance()));
        System.out.print("Enter amount to withdraw: ₹");
        
        try {
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            
            if (amount <= 0) {
                System.out.println("❌ Invalid amount. Please enter a positive value.");
                return;
            }
            
            if (amount > currentUser.getBalance()) {
                System.out.println("❌ Insufficient balance.");
                return;
            }
            
            if (currentUser.getBalance() - amount < 1000) {
                System.out.println("❌ Minimum balance of ₹1000 must be maintained.");
                return;
            }
            
            // Update balance in database
            String query = "UPDATE users SET balance = balance - ? WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setDouble(1, amount);
            stmt.setInt(2, currentUser.getUserId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                currentUser.setBalance(currentUser.getBalance() - amount);
                logTransaction(currentUser.getUserId(), "WITHDRAWAL", amount, "Cash withdrawal");
                
                System.out.println("✓ Withdrawal successful!");
                System.out.println("Amount Withdrawn: ₹" + String.format("%.2f", amount));
                System.out.println("New Balance: ₹" + String.format("%.2f", currentUser.getBalance()));
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Withdrawal failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Please enter a valid amount.");
            scanner.nextLine(); // Clear buffer
        }
        
        pressEnterToContinue();
    }
    
    // Transfer funds
    private static void transferFunds() {
        System.out.println("\n═══════════════ FUND TRANSFER ═══════════════");
        System.out.println("Available Balance: ₹" + String.format("%.2f", currentUser.getBalance()));
        
        try {
            System.out.print("Enter recipient account number: ");
            String recipientAccount = scanner.nextLine();
            
            // Check if recipient exists
            String checkQuery = "SELECT * FROM users WHERE account_number = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, recipientAccount);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("❌ Recipient account not found.");
                return;
            }
            
            if (recipientAccount.equals(currentUser.getAccountNumber())) {
                System.out.println("❌ Cannot transfer to your own account.");
                return;
            }
            
            int recipientId = rs.getInt("user_id");
            String recipientName = rs.getString("full_name");
            
            System.out.println("Recipient: " + recipientName);
            System.out.print("Enter amount to transfer: ₹");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            
            if (amount <= 0) {
                System.out.println("❌ Invalid amount. Please enter a positive value.");
                return;
            }
            
            if (amount > currentUser.getBalance()) {
                System.out.println("❌ Insufficient balance.");
                return;
            }
            
            if (currentUser.getBalance() - amount < 1000) {
                System.out.println("❌ Minimum balance of ₹1000 must be maintained.");
                return;
            }
            
            // Start transaction (ACID compliance)
            connection.setAutoCommit(false);
            
            try {
                // Deduct from sender
                String deductQuery = "UPDATE users SET balance = balance - ? WHERE user_id = ?";
                PreparedStatement deductStmt = connection.prepareStatement(deductQuery);
                deductStmt.setDouble(1, amount);
                deductStmt.setInt(2, currentUser.getUserId());
                deductStmt.executeUpdate();
                
                // Add to recipient
                String addQuery = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
                PreparedStatement addStmt = connection.prepareStatement(addQuery);
                addStmt.setDouble(1, amount);
                addStmt.setInt(2, recipientId);
                addStmt.executeUpdate();
                
                // Commit transaction
                connection.commit();
                connection.setAutoCommit(true);
                
                currentUser.setBalance(currentUser.getBalance() - amount);
                
                // Log transactions
                logTransaction(currentUser.getUserId(), "TRANSFER_SENT", amount, "Transfer to " + recipientAccount);
                logTransaction(recipientId, "TRANSFER_RECEIVED", amount, "Transfer from " + currentUser.getAccountNumber());
                
                System.out.println("✓ Transfer successful!");
                System.out.println("Amount Transferred: ₹" + String.format("%.2f", amount));
                System.out.println("Recipient: " + recipientName);
                System.out.println("New Balance: ₹" + String.format("%.2f", currentUser.getBalance()));
                
            } catch (SQLException e) {
                // Rollback on error
                connection.rollback();
                connection.setAutoCommit(true);
                System.out.println("❌ Transfer failed: " + e.getMessage());
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Transfer failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Invalid input.");
            scanner.nextLine(); // Clear buffer
        }
        
        pressEnterToContinue();
    }
    
    // View transaction history
    private static void viewTransactionHistory() {
        System.out.println("\n═══════════════ TRANSACTION HISTORY ═══════════════");
        
        try {
            String query = "SELECT * FROM transactions WHERE user_id = ? ORDER BY transaction_date DESC LIMIT 10";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, currentUser.getUserId());
            
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("─────────────────────────────────────────────────────────────────────");
            System.out.printf("%-20s %-20s %-15s %s%n", "Date/Time", "Type", "Amount", "Description");
            System.out.println("─────────────────────────────────────────────────────────────────────");
            
            boolean hasTransactions = false;
            while (rs.next()) {
                hasTransactions = true;
                String date = rs.getTimestamp("transaction_date").toString();
                String type = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");
                String description = rs.getString("description");
                
                System.out.printf("%-20s %-20s ₹%-14.2f %s%n", date, type, amount, description);
            }
            
            if (!hasTransactions) {
                System.out.println("No transactions found.");
            }
            
            System.out.println("─────────────────────────────────────────────────────────────────────");
            
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch transactions: " + e.getMessage());
        }
        
        pressEnterToContinue();
    }
    
    // Change password
    private static void changePassword() {
        System.out.println("\n═══════════════ CHANGE PASSWORD ═══════════════");
        
        try {
            System.out.print("Enter current password: ");
            String currentPassword = scanner.nextLine();
            
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();
            
            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine();
            
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("❌ New passwords do not match.");
                return;
            }
            
            // Verify current password
            String hashedCurrentPassword = hashPassword(currentPassword);
            String checkQuery = "SELECT password FROM users WHERE user_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, currentUser.getUserId());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (!storedPassword.equals(hashedCurrentPassword)) {
                    System.out.println("❌ Current password is incorrect.");
                    return;
                }
            }
            
            // Update password
            String hashedNewPassword = hashPassword(newPassword);
            String updateQuery = "UPDATE users SET password = ? WHERE user_id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setString(1, hashedNewPassword);
            updateStmt.setInt(2, currentUser.getUserId());
            
            int rowsAffected = updateStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Password changed successfully!");
                logTransaction(currentUser.getUserId(), "PASSWORD_CHANGED", 0, "User changed password");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Password change failed: " + e.getMessage());
        }
        
        pressEnterToContinue();
    }
    
    // Admin panel
    private static void adminPanel() {
        System.out.println("\n═══════════════ ADMIN PANEL ═══════════════");
        
        try {
            String query = "SELECT account_number, full_name, email, balance, role FROM users";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            System.out.println("─────────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-15s %-25s %-30s %-15s %s%n", "Account No", "Name", "Email", "Balance", "Role");
            System.out.println("─────────────────────────────────────────────────────────────────────────────");
            
            while (rs.next()) {
                System.out.printf("%-15s %-25s %-30s ₹%-14.2f %s%n",
                    rs.getString("account_number"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getDouble("balance"),
                    rs.getString("role")
                );
            }
            
            System.out.println("─────────────────────────────────────────────────────────────────────────────");
            
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch user data: " + e.getMessage());
        }
        
        pressEnterToContinue();
    }
    
    // Logout
    private static void logout() {
        logTransaction(currentUser.getUserId(), "LOGOUT", 0, "User logged out");
        System.out.println("\n✓ Logged out successfully. See you soon!");
        currentUser = null;
    }
    
    // Log transaction
    private static void logTransaction(int userId, String type, double amount, String description) {
        try {
            String query = "INSERT INTO transactions (user_id, transaction_type, amount, description) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.setString(4, description);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Warning: Failed to log transaction: " + e.getMessage());
        }
    }
    
    // Generate account number
    private static String generateAccountNumber() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 9000) + 1000;
        return "SB" + timestamp % 1000000 + random;
    }
    
    // Hash password using SHA-256
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    // Helper method to pause
    private static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    // Close database connection
    private static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}

// User class (OOP)
class User {
    private int userId;
    private String accountNumber;
    private String fullName;
    private String email;
    private double balance;
    private String role;
    
    public User(int userId, String accountNumber, String fullName, String email, double balance, String role) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.email = email;
        this.balance = balance;
        this.role = role;
    }
    
    // Getters
    public int getUserId() { return userId; }
    public String getAccountNumber() { return accountNumber; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public double getBalance() { return balance; }
    public String getRole() { return role; }
    
    // Setters
    public void setBalance(double balance) { this.balance = balance; }
}