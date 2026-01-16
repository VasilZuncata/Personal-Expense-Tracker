import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

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
        // ---- UI ONLY: Nimbus + tuned defaults ----
        setNimbusLookAndDefaults();

        setTitle("Personal Expense Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Fixed size (no fullscreen / no resize)
        setSize(820, 560);
        setResizable(false);
        setLocationRelativeTo(null);

        // Root container with padding
        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(new EmptyBorder(2, 2, 2, 2));

        // ===== EXPENSES TAB =====
        JPanel expensesTab = new JPanel(new BorderLayout(14, 14));
        expensesTab.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Top: Add Expense card ---
        JPanel formCard = new JPanel(new BorderLayout());
        formCard.setBorder(createCardBorder("Add Expense"));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(12, 12, 6, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel catLbl = new JLabel("Category:");
        JLabel amtLbl = new JLabel("Amount:");
        JLabel noteLbl = new JLabel("Note:");

        catLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        amtLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        noteLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        categoryField.setColumns(18);
        amountField.setColumns(18);
        noteField.setColumns(18);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(catLbl, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        form.add(categoryField, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(amtLbl, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        form.add(amountField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        form.add(noteLbl, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        form.add(noteField, gbc);

        // Buttons (same actions, better layout)
        JButton addBtn = new JButton("Add");
        JButton delBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");

        styleButton(addBtn, true);
        styleButton(delBtn, false);
        styleButton(saveBtn, false);
        styleButton(loadBtn, false);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setBorder(new EmptyBorder(0, 12, 12, 12));
        buttonRow.add(addBtn);
        buttonRow.add(delBtn);
        buttonRow.add(saveBtn);
        buttonRow.add(loadBtn);

        formCard.add(form, BorderLayout.CENTER);
        formCard.add(buttonRow, BorderLayout.SOUTH);

        expensesTab.add(formCard, BorderLayout.NORTH);

        // --- Center: Expenses list card ---
        JPanel listCard = new JPanel(new BorderLayout());
        listCard.setBorder(createCardBorder("Expenses"));

        expenseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseList.setFixedCellHeight(32);
        expenseList.setBorder(new EmptyBorder(6, 6, 6, 6));

        // UI-only: nicer list row padding
        expenseList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus
            ) {
                JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBorder(new EmptyBorder(6, 10, 6, 10));
                return c;
            }
        });

        JScrollPane listScroll = new JScrollPane(expenseList);
        listScroll.setBorder(new EmptyBorder(12, 12, 12, 12));
        listCard.add(listScroll, BorderLayout.CENTER);

        expensesTab.add(listCard, BorderLayout.CENTER);

        // --- Bottom: status bar style stats ---
        JPanel statsBar = new JPanel(new BorderLayout());
        statsBar.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(210, 210, 210)),
                new EmptyBorder(10, 12, 10, 12)
        ));

        Font statsFont = totalLabel.getFont().deriveFont(Font.BOLD, totalLabel.getFont().getSize() + 1f);
        totalLabel.setFont(statsFont);
        averageLabel.setFont(statsFont);

        statsBar.add(totalLabel, BorderLayout.WEST);
        statsBar.add(averageLabel, BorderLayout.EAST);

        expensesTab.add(statsBar, BorderLayout.SOUTH);

        tabs.addTab("Expenses", expensesTab);

        // ===== CATEGORY SUMMARY TAB =====
        categoryPanel = new CategorySummaryPanel(expenses);

        // wrap with padding so it matches the rest
        JPanel categoryWrapper = new JPanel(new BorderLayout());
        categoryWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        categoryWrapper.add(categoryPanel, BorderLayout.CENTER);

        tabs.addTab("Category Summary", categoryWrapper);

        root.add(tabs, BorderLayout.CENTER);

        // ===== ACTIONS (UNCHANGED) =====
        addBtn.addActionListener(e -> addExpense());
        delBtn.addActionListener(e -> deleteExpense());
        saveBtn.addActionListener(e -> ExpenseStorage.save(expenses, this));
        loadBtn.addActionListener(e -> loadExpenses());
    }

    // ---------------- UI helpers (UI only) ----------------

    private static void setNimbusLookAndDefaults() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Slightly cleaner overall typography (still Nimbus)
        Font base = UIManager.getFont("Label.font");
        if (base != null) {
            Font ui = base.deriveFont(base.getSize2D() + 1f);
            UIManager.put("Label.font", ui);
            UIManager.put("Button.font", ui);
            UIManager.put("TextField.font", ui);
            UIManager.put("List.font", ui);
            UIManager.put("TabbedPane.font", ui);
        }

        // Nudge some spacing defaults
        UIManager.put("TabbedPane.tabInsets", new Insets(10, 16, 10, 16));
    }

    private static Border createCardBorder(String title) {
        Border outer = new LineBorder(new Color(210, 210, 210), 1, true);
        Border inner = new EmptyBorder(8, 10, 10, 10);
        TitledBorder titled = BorderFactory.createTitledBorder(outer, title);
        titled.setTitleFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD));
        titled.setTitleColor(new Color(70, 70, 70));
        return new CompoundBorder(titled, inner);
    }

    private static void styleButton(JButton b, boolean primary) {
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(110, 34));

        // UI-only: give the primary button a subtle emphasis using font weight
        if (primary) {
            b.setFont(b.getFont().deriveFont(Font.BOLD));
        }
    }

    // ---------------- Existing logic (UNCHANGED) ----------------

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
