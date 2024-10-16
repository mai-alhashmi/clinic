import DB.DAO.PrescriptionDao;
import DB.DAO.UserDao;
import DB.DbConfig;
import DB.Patient;
import DB.Prescription;
import DB.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionFrame extends JPanel {
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
    private JButton buttonClear;
    private JButton buttonDelete;
    private JButton buttonSave;
    private DefaultTableModel dtm;
    private JTable table;
    private Patient patient;

    public PrescriptionFrame(Patient patient) {
        this.patient = patient;
        setLayout(new BorderLayout());
        this.add(panel1);

        String[] columns = {"id", "medicine_name", "dosage", "duration", "Patient"};
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
            prescription.setPatient(patient);

            prescriptionDao.add(prescription);
            fillTable();
            JOptionPane.showMessageDialog(this, "Prescription added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving prescription: " + e.getMessage());
        }
    }

    private void fillTable() {
        dtm.setRowCount(0);
        try {
            Connection pr = DbConfig.createdConnection();
            PrescriptionDao prescriptionDao = new PrescriptionDao(pr);
            ArrayList<Prescription> prescriptions = prescriptionDao.getByPId(patient.getId());
            for (Prescription prescription : prescriptions) {
                String[] row = {
                        String.valueOf(prescription.getId()),
                        prescription.getMedicineName(),
                        prescription.getDosage(),
                        prescription.getDuration(),
                        prescription.getPatient().getName()

                };
                dtm.addRow(row);
            }
            pr.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading prescriptions: " + e.getMessage());
        }
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
}
//
