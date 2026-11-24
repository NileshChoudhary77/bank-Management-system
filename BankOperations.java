import com.mysql.cj.x.protobuf.MysqlxCrud.Update;

import java.math.BigDecimal;
import java.sql.*;

public class BankOperations {
    // public void createAccount(String name, BigDecimal openingBalance) {
    //     String sql = "INSERT INTO accounts(holder_name,balance) VALUES(?,?)";
    //     try (Connection c = DBConnection.getConnection();
    //          PreparedStatement p = c.prepareStatement(sql)) {
    //         p.setString(1, name);
    //         p.setBigDecimal(2, openingBalance);
    //         p.executeUpdate();
    //         System.out.println("Account created successfully.");
    //     } catch (SQLException e) {
    //         System.out.println("Error: " + e.getMessage());
    //     }
    // }



    public void createAccount(String name, BigDecimal openingBalance) {

    String sql = "INSERT INTO accounts (holder_name, balance) VALUES (?, ?)";

    try (Connection c = DBConnection.getConnection();
         PreparedStatement p = c.prepareStatement(
             sql, Statement.RETURN_GENERATED_KEYS)) {

        p.setString(1, name);
        p.setBigDecimal(2, openingBalance);
 
        p.executeUpdate();

        try (ResultSet rs = p.getGeneratedKeys()) {
            if (rs.next()) {
                int accountNumber = rs.getInt(1);

                System.out.println("Account created successfully!");
                System.out.println("Your Account Number: " + accountNumber);
            }
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


    public void viewAccount(int accountNo) {
        String sql = "SELECT * FROM accounts WHERE account_number=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, accountNo);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    BankAccount a = new BankAccount(r.getInt("account_number"),
                            r.getString("holder_name"), r.getBigDecimal("balance"));
                    System.out.println(a);
                } else System.out.println("Account not found.");
            }
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void deposit(int accountNo, BigDecimal amount) {
        String sql = "UPDATE accounts SET balance=balance+? WHERE account_number=?";
        updateBalance(sql, accountNo, amount, "Deposit successful.");
    }

    public void withdraw(int accountNo, BigDecimal amount) {
        String sql = "UPDATE accounts SET balance=balance-? WHERE account_number=? AND balance>=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setBigDecimal(1, amount); p.setInt(2, accountNo); p.setBigDecimal(3, amount);
            if (p.executeUpdate() == 1) System.out.println("Withdrawal successful.");
            else System.out.println("Account not found or insufficient balance.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private void updateBalance(String sql, int accountNo, BigDecimal amount, String success) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setBigDecimal(1, amount); p.setInt(2, accountNo);
            if (p.executeUpdate() == 1) System.out.println(success);
            else System.out.println("Account not found.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void checkBalance(int accountNo) {
        String sql = "SELECT balance FROM accounts WHERE account_number=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, accountNo);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) System.out.println("Balance: Rs. " + r.getBigDecimal(1));
            //                                                                  one means first column in sql query
                else System.out.println("Account not found.");
            }
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void transfer(int from, int to, BigDecimal amount) {
        if (from == to) { System.out.println("Accounts must be different."); return; }
        String debit = "UPDATE accounts SET balance=balance-? WHERE account_number=? AND balance>=?";
        String credit = "UPDATE accounts SET balance=balance+? WHERE account_number=?";
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int rows;
                try (PreparedStatement p = c.prepareStatement(debit)) {
                    p.setBigDecimal(1, amount); p.setInt(2, from); p.setBigDecimal(3, amount);
                    rows = p.executeUpdate();
                }
                if (rows != 1) throw new SQLException("Sender not found or insufficient balance.");
                try (PreparedStatement p = c.prepareStatement(credit)) {
                    p.setBigDecimal(1, amount); p.setInt(2, to);
                    if (p.executeUpdate() != 1) throw new SQLException("Receiver account not found.");
                }
                c.commit();
                System.out.println("Transfer successful.");
            } catch (SQLException e) {
                c.rollback();
                System.out.println("Transfer cancelled: " + e.getMessage());
            } finally { c.setAutoCommit(true); }
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void updateAccount(int accountNo, String newHolderName) {

    String sql = "UPDATE accounts SET holder_name = ? WHERE account_number = ?";

    try (Connection c = DBConnection.getConnection();
         PreparedStatement p = c.prepareStatement(sql)) {

        p.setString(1, newHolderName);
        p.setInt(2, accountNo);

        if (p.executeUpdate() == 1) {
            System.out.println("Account details updated successfully.");
        } else {
            System.out.println("Account not found.");
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
}

