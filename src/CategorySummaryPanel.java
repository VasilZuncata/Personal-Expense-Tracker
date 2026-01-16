import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;

public class CategorySummaryPanel extends JPanel {

    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> list = new JList<>(model);

    public CategorySummaryPanel(ArrayList<Expense> expenses) {
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new TitledBorder(
                new LineBorder(new Color(120, 120, 120, 60), 1, true),
                "Category Summary",
                TitledBorder.LEADING,
                TitledBorder.TOP,
                getFont().deriveFont(Font.BOLD)
        ));

        list.setFixedCellHeight(38);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus
            ) {
                JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBorder(new EmptyBorder(6, 12, 6, 12));
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
            model.addElement("No expenses yet.");
            return;
        }

        for (String key : map.keySet()) {
            model.addElement(key + ": " + String.format("%.2f", map.get(key)));
        }
    }
}
