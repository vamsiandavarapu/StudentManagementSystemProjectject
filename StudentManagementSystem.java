package studentmanagementSystem;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Student Data Class
class Student {
    String id, name, branch, phone;
    Student(String id, String name, String branch, String phone) {
        this.id = id; this.name = name; this.branch = branch; this.phone = phone;
    }
}

// Background Panel
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(".\\background.jpg")).getImage();
        } catch (Exception e) {
            System.out.println("Background image not found.");
        }
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class StudentManagementSystem extends JFrame {
    private ArrayList<Student> students = new ArrayList<>();

    public StudentManagementSystem() {
        setTitle("Student Management System - Main Menu");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel background = new BackgroundPanel("/background.jpg");
        setContentPane(background);

        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));

        String[] options = {"Student Profile", "Fee Report", "Library System", "Marksheet"};
        for (String option : options) {
            JButton btn = createStyledButton(option);
            menuPanel.add(btn);

            if (option.equals("Student Profile")) {
                btn.addActionListener(e -> showStudentProfileUI());
            } else {
                btn.addActionListener(e -> JOptionPane.showMessageDialog(this, option + " module coming soon."));
            }
        }

        background.add(menuPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(new Color(0, 123, 255)); // Bootstrap blue
        btn.setForeground(Color.BLACK); // CHANGE TO BLACK
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        return btn;
    }

    private void showStudentProfileUI() {
        JFrame frame = new JFrame("Student Profile");
        frame.setSize(850, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        BackgroundPanel bg = new BackgroundPanel("/background.jpg");
        frame.setContentPane(bg);

        String[] labels = {"Student ID:", "Name:", "Branch:", "Phone:"};
        JTextField[] fields = new JTextField[4];
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);  // Transparent for background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setForeground(Color.WHITE);
            JTextField field = new JTextField(15);
            label.setForeground(Color.BLACK);


            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(label, gbc);
            gbc.gridx = 1;
            formPanel.add(field, gbc);
            fields[i] = field;
        }

        // Buttons
        String[] actions = {"Add", "Update", "Delete", "Clear"};
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        JButton[] btns = new JButton[4];
        for (int i = 0; i < actions.length; i++) {
            JButton b = new JButton(actions[i]);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setBackground(Color.decode("#007BFF"));
             b.setForeground(Color.BLACK); // set button text color to black

            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonPanel.add(b);
            btns[i] = b;
        }

        // Table
        String[] cols = {"Student ID", "Name", "Branch", "Phone"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(22);

        // 🎨 Table Styling
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setGridColor(Color.LIGHT_GRAY);

        table.getTableHeader().setBackground(new Color(200, 200, 255));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);

        // Add Button
        btns[0].addActionListener(e -> {
            String id = fields[0].getText().trim();
            String name = fields[1].getText().trim();
            String branch = fields[2].getText().trim();
            String phone = fields[3].getText().trim();

            if (id.isEmpty() || name.isEmpty() || branch.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Student s : students) {
                if (s.id.equals(id)) {
                    JOptionPane.showMessageDialog(frame, "Student ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            students.add(new Student(id, name, branch, phone));
            model.addRow(new Object[]{id, name, branch, phone});
            for (JTextField f : fields) f.setText("");
        });

        // Update Button
        btns[1].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select a row to update.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String id = fields[0].getText().trim();
            String name = fields[1].getText().trim();
            String branch = fields[2].getText().trim();
            String phone = fields[3].getText().trim();

            Student s = students.get(row);
            s.id = id;
            s.name = name;
            s.branch = branch;
            s.phone = phone;

            model.setValueAt(id, row, 0);
            model.setValueAt(name, row, 1);
            model.setValueAt(branch, row, 2);
            model.setValueAt(phone, row, 3);

            for (JTextField f : fields) f.setText("");
        });

        // Delete
        btns[2].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select a row to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            students.remove(row);
            model.removeRow(row);
            for (JTextField f : fields) f.setText("");
        });

        // Clear
        btns[3].addActionListener(e -> {
            for (JTextField f : fields) f.setText("");
        });

        // Table Row Selection
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                for (int i = 0; i < 4; i++) {
                    fields[i].setText((String) model.getValueAt(row, i));
                }
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        bg.add(topPanel, BorderLayout.NORTH);
        bg.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new StudentManagementSystem().setVisible(true));
    }
}

