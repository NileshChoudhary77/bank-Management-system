import java.math.BigDecimal;

public class BankAccount {
    private final int accountNumber;
    private final String holderName;
    private final BigDecimal balance;

    public BankAccount(int accountNumber, String holderName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public int getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public BigDecimal getBalance() { return balance; }

    public String toString() {
        return "Account Number: " + accountNumber + "\nHolder: " + holderName
                + "\nBalance: Rs. " + balance;
    }
}
