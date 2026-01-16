import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class CategorySummaryPanel extends JPanel {

    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> list = new JList<>(model);

    public CategorySummaryPanel(ArrayList<Expense> expenses) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Category Summary"));

        add(new JScrollPane(list), BorderLayout.CENTER);
        updateSummary(expenses);
    }

    public void updateSummary(ArrayList<Expense> expenses) {
        model.clear();
        HashMap<String, Double> map = new HashMap<>();

        for (Expense e : expenses) {
            map.put(e.category, map.getOrDefault(e.category, 0.0) + e.amount);
        }

        for (String key : map.keySet()) {
            model.addElement(key + ": " + String.format("%.2f", map.get(key)));
        }
    }
}
