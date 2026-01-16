import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class ExpenseTrackerGUI extends JFrame {

    // ---- THEME STATE (UI only) ----
    private static boolean darkMode = true;

    private ArrayList<Expense> expenses = new ArrayList<>();
    private DefaultListModel<Expense> listModel = new DefaultListModel<>();

    private JTextField categoryField = new JTextField();
    private JTextField amountField = new JTextField();
    private JTextField noteField = new JTextField();

    private JLabel totalLabel = new JLabel("Total: 0.00");
    private JLabel averageLabel = new JLabel("Average: 0.00");

    private JList<Expense> expenseList = new JList<>(listModel);
    private CategorySummaryPanel categoryPanel;

    // Theme toggle button
    private JToggleButton themeToggle = new JToggleButton();

    public ExpenseTrackerGUI() {
        // Set theme before components
        applyTheme(darkMode);

        setTitle("Expense Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920, 610);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        setContentPane(root);

        // -------- Header (title + theme toggle) --------
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(0, 2, 0, 2));

        JLabel title = new JLabel("Personal Expense Tracker");
        title.setFont(title.getFont().deriveFont(Font.BOLD, title.getFont().getSize() + 6f));
        header.add(title, BorderLayout.WEST);

        themeToggle.setSelected(darkMode);
        updateToggleText();
        themeToggle.setFocusPainted(false);
        themeToggle.setPreferredSize(new Dimension(160, 36));
        themeToggle.addActionListener(e -> {
            darkMode = themeToggle.isSelected();
            updateToggleText();
            switchTheme(darkMode);
        });

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        headerRight.add(themeToggle);
        header.add(headerRight, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // -------- Tabs --------
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(null);

        // ===================== EXPENSES TAB =====================
        JPanel expensesTab = new JPanel(new BorderLayout(16, 16));

        // Add Expense card
        JPanel addCard = card("Add Expense");
        addCard.setLayout(new BorderLayout());
        addCard.setBorder(new EmptyBorder(24, 0, 16, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 16, 8, 16));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        addField(form, g, "Category", categoryField, 0);
        addField(form, g, "Amount", amountField, 1);
        addField(form, g, "Note", noteField, 2);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBorder(new EmptyBorder(0, 16, 16, 16));

        JButton addBtn = primaryButton("Add");
        JButton delBtn = normalButton("Delete");
        JButton saveBtn = normalButton("Save");
        JButton loadBtn = normalButton("Load");

        actions.add(addBtn);
        actions.add(delBtn);
        actions.add(saveBtn);
        actions.add(loadBtn);

        addCard.add(form, BorderLayout.CENTER);
        addCard.add(actions, BorderLayout.SOUTH);

     
        JPanel listCard = card("Expenses");
        listCard.setLayout(new BorderLayout());

        expenseList.setFixedCellHeight(38);
        expenseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseList.setCellRenderer(new ModernListRenderer());

        JScrollPane scroll = new JScrollPane(expenseList);
        scroll.setBorder(new EmptyBorder(12, 12, 12, 12));
        listCard.add(scroll, BorderLayout.CENTER);

        
        JPanel status = new JPanel(new BorderLayout());
        status.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(80, 80, 80, 60)),
                new EmptyBorder(10, 14, 10, 14)
        ));

        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD));
        averageLabel.setFont(averageLabel.getFont().deriveFont(Font.BOLD));

        status.add(totalLabel, BorderLayout.WEST);
        status.add(averageLabel, BorderLayout.EAST);

        expensesTab.add(addCard, BorderLayout.NORTH);
        expensesTab.add(listCard, BorderLayout.CENTER);
        expensesTab.add(status, BorderLayout.SOUTH);

        tabs.addTab("Expenses", expensesTab);

        // ===================== CATEGORY TAB =====================
        categoryPanel = new CategorySummaryPanel(expenses);
        tabs.addTab("Category Summary", categoryPanel);

        root.add(tabs, BorderLayout.CENTER);

        // ===================== ACTIONS (UNCHANGED) =====================
        addBtn.addActionListener(e -> addExpense());
        delBtn.addActionListener(e -> deleteExpense());
        saveBtn.addActionListener(e -> ExpenseStorage.save(expenses, this));
        loadBtn.addActionListener(e -> loadExpenses());
    }

    // ---------------- THEME METHODS (UI only) ----------------

    private void updateToggleText() {
        themeToggle.setText(darkMode ? "Dark Mode: ON" : "Dark Mode: OFF");
    }

    private static void applyTheme(boolean dark) {
        try {
            if (dark) {
                FlatDarculaLaf.setup();
            } else {
                FlatLightLaf.setup();
            }

           
            UIManager.put("Button.arc", 14);
            UIManager.put("Component.arc", 14);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("TabbedPane.tabInsets", new Insets(10, 16, 10, 16));
        } catch (Exception ignored) {}
    }

    private void switchTheme(boolean dark) {
        applyTheme(dark);

     
        SwingUtilities.updateComponentTreeUI(this);

       
        invalidate();
        validate();
        repaint();
    }

    // ---------------- UI HELPERS ----------------

    private static JPanel card(String title) {
        JPanel p = new JPanel();
        p.setBorder(new TitledBorder(
                new LineBorder(new Color(120, 120, 120, 60), 1, true),
                title,
                TitledBorder.LEADING,
                TitledBorder.TOP,
                p.getFont().deriveFont(Font.BOLD)
        ));
        return p;
    }

    private static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(120, 38));
        b.setFont(b.getFont().deriveFont(Font.BOLD));
        b.setFocusPainted(false);
        return b;
    }

    private static JButton normalButton(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(120, 38));
        b.setFocusPainted(false);
        return b;
    }

    private static void addField(JPanel panel, GridBagConstraints g, String label, JTextField field, int row) {
        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0;
        panel.add(new JLabel(label), g);

        g.gridx = 1;
        g.gridy = row;
        g.weightx = 1;
        panel.add(field, g);
    }

    // ---------------- LOGIC (UNCHANGED) ----------------

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
        applyTheme(darkMode);
        SwingUtilities.invokeLater(() -> new ExpenseTrackerGUI().setVisible(true));
    }

    // ---------------- MODERN LIST RENDERER ----------------
    private static class ModernListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus
        ) {
            JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            c.setBorder(new EmptyBorder(6, 12, 6, 12));
            return c;
        }
    }
}
