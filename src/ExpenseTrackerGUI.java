import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

class Expense {
    String category;
    double amount;
    String note;

    Expense(String category, double amount, String note) {
        this.category = category;
        this.amount = amount;
        this.note = note;
    }

    @Override
    public String toString() {
        return category + " - " + amount + " (" + note + ")";
    }
}

public class ExpenseTrackerGUI extends JFrame {

    private ArrayList<Expense> expenses = new ArrayList<>();
    private DefaultListModel<Expense> listModel = new DefaultListModel<>();

    private JTextField categoryField = new JTextField();
    private JTextField amountField = new JTextField();
    private JTextField noteField = new JTextField();

    private JLabel totalLabel = new JLabel("Total: 0.00");
    private JLabel averageLabel = new JLabel("Average: 0.00");

    private JList<Expense> expenseList = new JList<>(listModel);

    public ExpenseTrackerGUI() {
        setTitle("Personal Expense Tracker");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // === TOP PANEL ===
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Note:"));
        inputPanel.add(noteField);

        JButton addButton = new JButton("Add Expense");
        JButton deleteButton = new JButton("Delete Selected");

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        // === CENTER PANEL ===
        JScrollPane scrollPane = new JScrollPane(expenseList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Expenses"));

        // === BOTTOM PANEL ===
        JPanel statsPanel = new JPanel(new GridLayout(1, 2));
        statsPanel.add(totalLabel);
        statsPanel.add(averageLabel);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);

        // === ACTIONS ===
        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteExpense());
    }

    private void addExpense() {
        String category = categoryField.getText();
        String note = noteField.getText();

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Expense expense = new Expense(category, amount, note);
        expenses.add(expense);
        listModel.addElement(expense);

        categoryField.setText("");
        amountField.setText("");
        noteField.setText("");

        updateStats();
    }

    private void deleteExpense() {
        int index = expenseList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Select an expense to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        expenses.remove(index);
        listModel.remove(index);
        updateStats();
    }

    private void updateStats() {
        if (expenses.isEmpty()) {
            totalLabel.setText("Total: 0.00");
            averageLabel.setText("Average: 0.00");
            return;
        }

        double total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }

        double average = total / expenses.size();

        totalLabel.setText("Total: " + String.format("%.2f", total));
        averageLabel.setText("Average: " + String.format("%.2f", average));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExpenseTrackerGUI().setVisible(true);
        });
    }
}
