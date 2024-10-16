import DB.Clinic;
import DB.DAO.ClinicDao;
import DB.DbConfig;
import DB.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//
public class ClinicFrame extends JFrame {
    private JPanel panel1;
    private JPanel panelNorth;
    private JTextField textFieldName;
    private JTextField textFieldSpecialty;
    private JTextField textFieldAddress;
    private JTextField textFieldPhone;
    private JTextField textFieldMobile;
    private JTextField textFieldEmail;
    private JTextField textFieldDName;
    private JPanel panelWest;
    private JButton buttonSave;
    private JTextPane textPane1;
    private JButton buttonClear;
    private JButton buttonDelete;
    private JPanel PanelSouth;
    private JButton buttonHome;
    private DefaultTableModel dtm;
    private JTable table;
private Users user;
    public ClinicFrame(Users user) {
        this.user=user;
        this.setBounds(150, 100, 700, 500);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().add(panel1);
        String[] columns = {"id", "name", "specialty", "address", "phone", "mobile", "email", "doctor_name"};
        dtm = new DefaultTableModel(null, columns);
        table = new JTable(dtm);
        Font font = new Font("Arial", Font.PLAIN, 20);
        table.setFont(font);
        table.setForeground(Color.DARK_GRAY);
        table.getTableHeader().setFont(font);
        table.getTableHeader().setBackground(new Color(126, 136, 236));

        fillTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel1.add(scrollPane);

        buttonSave.addActionListener(e -> save());
        buttonClear.addActionListener(e -> clearFields());
        buttonDelete.addActionListener(e -> deleteClinic());
        buttonHome.addActionListener(e -> goToHome());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goToHome();
            }
        });
  }
    private void save() {
        try (Connection cn = DbConfig.createdConnection()) {
            ClinicDao clinicDao = new ClinicDao(cn);
            Clinic clinic = new Clinic();
            clinic.setName(textFieldName.getText());
            clinic.setSpecialty(textFieldSpecialty.getText());
            clinic.setAddress(textFieldAddress.getText());
            clinic.setPhone(textFieldPhone.getText());
            clinic.setMobile(textFieldMobile.getText());
            clinic.setEmail(textFieldEmail.getText());
            clinic.setDoctorName(textFieldDName.getText());
            clinicDao.add(clinic);
            fillTable();
            JOptionPane.showMessageDialog(this, "Clinic added successfully");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding clinic: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void fillTable() {
        dtm.setRowCount(0);
        try (Connection cn = DbConfig.createdConnection()) {
            ClinicDao clinicDao = new ClinicDao(cn);
            ArrayList<Clinic> arrayList = clinicDao.get();
            for (Clinic clinic : arrayList) {
                String[] row = {
                        clinic.getId() + "",
                        clinic.getName(),
                        clinic.getSpecialty(),
                        clinic.getAddress(),
                        clinic.getPhone(),
                        clinic.getMobile(),
                        clinic.getEmail(),
                        clinic.getDoctorName()
                };
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving clinics: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        textFieldName.setText("");
        textFieldSpecialty.setText("");
        textFieldAddress.setText("");
        textFieldPhone.setText("");
        textFieldMobile.setText("");
        textFieldEmail.setText("");
        textFieldDName.setText("");
    }
    private void deleteClinic() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this clinic?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String clinicId = (String) table.getValueAt(selectedRow, 0);
                try (Connection cn = DbConfig.createdConnection()) {
                    ClinicDao clinicDao = new ClinicDao(cn);
                    clinicDao.delete(Integer.parseInt(clinicId));
                    dtm.removeRow(selectedRow);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error deleting clinic: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void goToHome() {
        MainFrame m = new MainFrame(user);
        m.pack();
        m.setVisible(true);
       // this.dispose(); // Close the current frame
    }


}
