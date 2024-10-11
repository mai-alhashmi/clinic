import DB.DAO.PrescriptionDao;
import DB.DAO.UserDao;
import DB.DbConfig;
import DB.Prescription;
import DB.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionFrame extends JFrame {
    private JPanel panel1;
    private JPanel PanelNorth;
    private JPanel PanelWest;
    private JTextField textFieldId;
    private JTextField textFieldMedicinName;
    private JTextField textFieldDosage;
    private JTextField textFieldDuration;
    private JTextField textFieldUserId;
    private JLabel medicineName;
    private JLabel dosageLabel;
    private JLabel duration;
    private JLabel userId;
    private JButton buttonClear;
    private JButton buttonDelete;
    private JButton buttonSave;
    private DefaultTableModel dtm;
    private JTable table;


public PrescriptionFrame() {
    this.add(panel1);
        this.setBounds(150, 50, 700, 600);
        this.getContentPane().add(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        String[] columns = {"id", "medicine_name", "dosage", "duration", "user_id"};
        dtm = new DefaultTableModel(null, columns);
        table = new JTable(dtm);
        Font font = new Font("Arial", Font.PLAIN, 20);
        table.setFont(font);
        table.setForeground(Color.DARK_GRAY);
        table.getTableHeader().setFont(font);
        table.getTableHeader().setBackground(new Color(245, 190, 126));

        fillTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel1.add(scrollPane);
        buttonSave.addActionListener(e -> save());
        buttonDelete.addActionListener(e -> delete());
        buttonClear.addActionListener(e -> clearFields());
    }
    private void save() {
        try {
            Connection pr = DbConfig.createdConnection();
            PrescriptionDao prescriptionDao = new PrescriptionDao(pr);
            Prescription prescription = new Prescription();

            prescription.setMedicineName(textFieldMedicinName.getText());
            prescription.setDosage(textFieldDosage.getText());
            prescription.setDuration(textFieldDuration.getText());
           // prescription.setUserId(Integer.parseInt(textFieldUserId.getText()));
            prescriptionDao.add(prescription);
            fillTable();
            JOptionPane.showMessageDialog(this, "Prescription added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving prescription: " + e.getMessage());
        }
    }
    private void fillTable() {
        dtm.setRowCount(0);
        try {
            Connection pr = DbConfig.createdConnection();
            PrescriptionDao prescriptionDao = new PrescriptionDao(pr);

            ArrayList<Prescription> prescriptions = prescriptionDao.get();
            for (Prescription prescription : prescriptions) {
                String[] row = {
                        String.valueOf(prescription.getId()),
                        prescription.getMedicineName(),
                        prescription.getDosage(),
                        prescription.getDuration(),
                      //  String.valueOf(prescription.getUserId())
                };
                dtm.addRow(row);
            }
            pr.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading prescriptions: " + e.getMessage());
        }
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (row >= 0 && row < table.getRowCount() && col >= 0 && col < table.getColumnCount()) {
                String newValue = (String) table.getValueAt(row, col);
                int prescriptionId = Integer.parseInt((String) table.getValueAt(row, 0));

                try {
                    Connection pr = DbConfig.createdConnection();
                    PrescriptionDao prescriptionDao = new PrescriptionDao(pr);
                    Prescription prescription = prescriptionDao.get(prescriptionId);
                    if (col == 1) {
                        prescription.setMedicineName(newValue);
                    } else if (col == 2) {
                        prescription.setDosage(newValue);
                    } else if (col == 3) {
                        prescription.setDuration(newValue);
                    } else if (col == 4) {
                     //   prescription.setUserId(Integer.parseInt(newValue));
                    }

                    prescriptionDao.update(prescription); // تحديث الوصفة في قاعدة البيانات
                    pr.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(this, "Error updating prescription: " + exception.getMessage());
                }
            }
        });
    }
    private void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String prescriptionId = (String) table.getValueAt(selectedRow, 0);
            try (Connection pr = DbConfig.createdConnection()) {
                PrescriptionDao prescriptionDao = new PrescriptionDao(pr);
                prescriptionDao.delete(Integer.parseInt(prescriptionId));
                dtm.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Prescription deleted successfully.");
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(this, "Error deleting prescription: " + exception.getMessage());

            }
        } else {
            JOptionPane.showMessageDialog(this, "No prescription selected to delete.");
        }
    }
    private void clearFields() {
        textFieldId.setText("");
        textFieldMedicinName.setText("");
        textFieldDosage.setText("");
        textFieldDuration.setText("");
        textFieldUserId.setText("");
    }

    public static void main(String[] args) {
        PrescriptionFrame prescriptionFrame = new PrescriptionFrame();
        prescriptionFrame.setVisible(true);
        }
    }
