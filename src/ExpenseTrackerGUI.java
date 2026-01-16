import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class ExpenseTrackerGUI extends JFrame {

    private ArrayList<Expense> expenses = new ArrayList<>();
    private DefaultListModel<Expense> listModel = new DefaultListModel<>();

    private JTextField categoryField = new JTextField();
    private JTextField amountField = new JTextField();
    private JTextField noteField = new JTextField();

    private JLabel totalLabel = new JLabel("Total: 0.00");
    private JLabel averageLabel = new JLabel("Average: 0.00");

    private JList<Expense> expenseList = new JList<>(listModel);
    private CategorySummaryPanel categoryPanel;

    public ExpenseTrackerGUI() {
        setTitle("Personal Expense Tracker");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // ===== EXPENSES TAB =====
        JPanel expensesTab = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Note:"));
        inputPanel.add(noteField);

        JButton addBtn = new JButton("Add");
        JButton delBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");

        inputPanel.add(addBtn);
        inputPanel.add(delBtn);
        inputPanel.add(saveBtn);
        inputPanel.add(loadBtn);

        expensesTab.add(inputPanel, BorderLayout.NORTH);
        expensesTab.add(new JScrollPane(expenseList), BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(1, 2));
        statsPanel.add(totalLabel);
        statsPanel.add(averageLabel);

        expensesTab.add(statsPanel, BorderLayout.SOUTH);
        tabs.addTab("Expenses", expensesTab);

        // ===== CATEGORY SUMMARY TAB =====
        categoryPanel = new CategorySummaryPanel(expenses);
        tabs.addTab("Category Summary", categoryPanel);

        add(tabs);

        // ===== ACTIONS =====
        addBtn.addActionListener(e -> addExpense());
        delBtn.addActionListener(e -> deleteExpense());
        saveBtn.addActionListener(e -> ExpenseStorage.save(expenses, this));
        loadBtn.addActionListener(e -> loadExpenses());
    }

    private void addExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) throw new NumberFormatException();

            Expense e = new Expense(categoryField.getText(), amount, noteField.getText());
            expenses.add(e);
            listModel.addElement(e);

            categoryField.setText("");
            amountField.setText("");
            noteField.setText("");

            updateStats();
            categoryPanel.updateSummary(expenses);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpense() {
        int i = expenseList.getSelectedIndex();
        if (i != -1) {
            expenses.remove(i);
            listModel.remove(i);
            updateStats();
            categoryPanel.updateSummary(expenses);
        }
    }

    private void loadExpenses() {
        ArrayList<Expense> loaded = ExpenseStorage.load(this);
        if (loaded != null) {
            expenses.clear();
            listModel.clear();
            expenses.addAll(loaded);
            for (Expense e : loaded) listModel.addElement(e);
            updateStats();
            categoryPanel.updateSummary(expenses);
        }
    }

    private void updateStats() {
        if (expenses.isEmpty()) {
            totalLabel.setText("Total: 0.00");
            averageLabel.setText("Average: 0.00");
            return;
        }

        double total = 0;
        for (Expense e : expenses) total += e.amount;

        totalLabel.setText("Total: " + String.format("%.2f", total));
        averageLabel.setText("Average: " + String.format("%.2f", total / expenses.size()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseTrackerGUI().setVisible(true));
    }
}
