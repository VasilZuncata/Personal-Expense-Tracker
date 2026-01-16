import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExpenseStorage {

    public static void save(ArrayList<Expense> expenses, JFrame parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Expense Files (*.exp)", "exp"));

        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().endsWith(".exp")) {
                file = new File(file.getAbsolutePath() + ".exp");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(expenses);
                JOptionPane.showMessageDialog(parent, "Expenses saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error saving file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Expense> load(JFrame parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Expense Files (*.exp)", "exp"));

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()))) {

                return (ArrayList<Expense>) ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(parent, "Error loading file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
}
