import java.io.Serializable;

public class Expense implements Serializable {
    String category;
    double amount;
    String note;

    public Expense(String category, double amount, String note) {
        this.category = category;
        this.amount = amount;
        this.note = note;
    }

    @Override
    public String toString() {
        return category + " - " + amount + " (" + note + ")";
    }
}
