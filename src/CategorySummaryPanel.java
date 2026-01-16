import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;

public class CategorySummaryPanel extends JPanel {

    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> list = new JList<>(model);

    public CategorySummaryPanel(ArrayList<Expense> expenses) {
        setLayout(new BorderLayout(14, 14));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(createCardBorder("Category Summary"));

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(32);
        list.setBorder(new EmptyBorder(6, 6, 6, 6));

        // UI-only: nicer padding per row
        list.setCellRenderer(new DefaultListCellRenderer() {
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

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(new EmptyBorder(12, 12, 12, 12));

        card.add(scroll, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        updateSummary(expenses);
    }

    public void updateSummary(ArrayList<Expense> expenses) {
        model.clear();
        HashMap<String, Double> map = new HashMap<>();

        for (Expense e : expenses) {
            map.put(e.category, map.getOrDefault(e.category, 0.0) + e.amount);
        }

        if (map.isEmpty()) {
            model.addElement("No expenses yet. Add some in the Expenses tab.");
            return;
        }

        for (String key : map.keySet()) {
            model.addElement(key + ": " + String.format("%.2f", map.get(key)));
        }
    }

    private static Border createCardBorder(String title) {
        Border outer = new LineBorder(new Color(210, 210, 210), 1, true);
        Border inner = new EmptyBorder(8, 10, 10, 10);
        TitledBorder titled = BorderFactory.createTitledBorder(outer, title);
        titled.setTitleFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD));
        titled.setTitleColor(new Color(70, 70, 70));
        return new CompoundBorder(titled, inner);
    }
}
