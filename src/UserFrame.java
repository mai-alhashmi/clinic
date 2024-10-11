import DB.DAO.UserDao;
import DB.DbConfig;
import DB.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserFrame extends JFrame{
    private JPanel panel1;
    private JPanel PanelNorth;
    private JTextField textFieldId;
    private JTextField textFieldEmail;
    private JTextField textFieldPassword;
    private JTextField textField4;
    private JComboBox comboBox1;
    private JPanel PanelWest;
    private JFormattedTextField formattedTextField1;
    private JTextField textFieldName;
    private JButton buttonSave;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JButton clearButton;
    private JButton deleteButton;
    private DefaultTableModel dtm;
    private JTable table;

    public UserFrame() {
        this.setBounds(150,100,700,500);
        this.getContentPane().add(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.add(panel1);

        String[] columns = {"id", "name", "role", "email", "password"};
        dtm = new DefaultTableModel(null, columns);
        table = new JTable(dtm);
        fillTable();

        JScrollPane scrollPane = new JScrollPane(table);
        panel1.add(scrollPane);
      //  this.getContentPane().add(panel1);

        buttonSave.addActionListener(e -> save());
        clearButton.addActionListener(e -> clearFields());
        deleteButton.addActionListener(e -> deleteUser());
    }

    private void save() {
        try (Connection u = DbConfig.createdConnection()) {
            UserDao userDao = new UserDao(u);
            Users user = new Users();
            if (textFieldName.getText().isEmpty() || textFieldEmail.getText().isEmpty() || textFieldPassword.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            user.setName(textFieldName.getText());
            user.setEmail(textFieldEmail.getText());
            user.setPassword(textFieldPassword.getText());
            userDao.add(user);
            fillTable();
            JOptionPane.showMessageDialog(this, "User added successfully");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void fillTable() {
        dtm.setRowCount(0);
        try (Connection u = DbConfig.createdConnection()) {
            UserDao userDao = new UserDao(u);
            ArrayList<Users> arrayList = userDao.get();
            for (Users user : arrayList) {
                String[] row = {
                        user.getId() + "",
                        user.getName(),
                        // user.getRole(),
                        user.getEmail(),
                        user.getPassword(),
                };
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void clearFields() {
        textFieldId.setText("");
        textFieldName.setText("");
        textFieldEmail.setText("");
        textFieldPassword.setText("");
    }

    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String userId = (String) table.getValueAt(selectedRow, 0);
            try (Connection u = DbConfig.createdConnection()) {
                UserDao userDao = new UserDao(u);
                userDao.delete(Integer.parseInt(userId));
                dtm.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "User deleted successfully");
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(this, "Error: " + exception.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    public static void main(String[] args) {
        UserFrame userFrame = new UserFrame();
        userFrame.setVisible(true);
    }
}

