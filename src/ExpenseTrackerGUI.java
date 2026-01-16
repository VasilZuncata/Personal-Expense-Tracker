import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class Expense implements Serializable {
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
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // === TOP PANEL ===
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Note:"));
        inputPanel.add(noteField);

        JButton addButton = new JButton("Add Expense");
        JButton deleteButton = new JButton("Delete Selected");
        JButton saveButton = new JButton("Save Expenses");
        JButton loadButton = new JButton("Load Expenses");

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(saveButton);
        inputPanel.add(loadButton);

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
        saveButton.addActionListener(e -> saveExpenses());
        loadButton.addActionListener(e -> loadExpenses());
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

    private void saveExpenses() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Expense Files (*.exp)", "exp");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".exp")) {
                file = new File(file.getAbsolutePath() + ".exp");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(expenses);
                JOptionPane.showMessageDialog(this, "Expenses saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadExpenses() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Expense Files (*.exp)", "exp");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                ArrayList<Expense> loadedExpenses = (ArrayList<Expense>) ois.readObject();
                
                expenses.clear();
                listModel.clear();
                expenses.addAll(loadedExpenses);
                
                for (Expense expense : loadedExpenses) {
                    listModel.addElement(expense);
                }
                
                updateStats();
                JOptionPane.showMessageDialog(this, "Expenses loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
