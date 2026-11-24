import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
        }
    }

    private static String readString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            return sc.nextLine().trim(); 
        }
    }

    private static BigDecimal readAmount(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                BigDecimal a = new BigDecimal(sc.nextLine().trim());
                if (a.compareTo(BigDecimal.ZERO) > 0) return a;
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter an amount greater than zero.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankOperations bank = new BankOperations();
        int choice;
        do {
            System.out.println("\n===== BANK MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. View Account Details");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Check Balance");
            System.out.println("6. Transfer Money");
            System.out.println("7. Update Account Details");
            System.out.println("8. Exit");
            choice = readInt(sc, "Enter choice: ");

            switch (choice) {
                case 1:
                    System.out.print("Account holder name: ");
                    String name = sc.nextLine().trim();
                    if (name.isEmpty()) { System.out.println("Name is required."); break; }
                    bank.createAccount(name, readAmount(sc, "Opening balance: Rs. "));
                    break;
                case 2: bank.viewAccount(readInt(sc, "Account number: ")); break;
                case 3:
                    bank.deposit(readInt(sc, "Account number: "), readAmount(sc, "Deposit amount: Rs. "));
                    break;
                case 4:
                    bank.withdraw(readInt(sc, "Account number: "), readAmount(sc, "Withdrawal amount: Rs. "));
                    break;
                case 5: bank.checkBalance(readInt(sc, "Account number: ")); break;
                case 6:
                    int from = readInt(sc, "Sender account number: ");
                    int to = readInt(sc, "Receiver account number: ");
                    bank.transfer(from, to, readAmount(sc, "Transfer amount: Rs. "));
                    break;
                case 7:
                    System.out.println("\n--- Update Account Details ---");

                    int accountNo = readInt(sc, "Account number: ");
                    String newName = readString(sc, "Enter new holder name: ");

                    if (newName.isEmpty()) {
                    System.out.println("Name cannot be empty.");
                    break;
                    }
                    bank.updateAccount(accountNo, newName);
                    break;
                       
                case 8: System.out.println("Thank you for using the bank system."); break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 8);
        sc.close();
    }
}
