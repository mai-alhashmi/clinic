
import DB.Clinic;
import DB.DAO.ClinicDao;
import DB.DAO.PatientDao;
import DB.DbConfig;
import DB.Patient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class PatientFrame extends JPanel {
    private JPanel panel1;
    private JTextField textFieldName;
    private JTextField textFieldBirthDate;
    private JTextField textFieldAddress;
    private JTextField textFieldPhone;
    private JTextField textFieldMobile;
    private JTextField textFieldEmail;
    private JTextField textFieldDisease;
    private JTextField textFieldMedicalDiagnosis;
    private JTextField textFieldCreatedAt;
    private JPanel PanelNorth;
    private JButton buttonClear;
    private JRadioButton radioButtonFemale;
    private JRadioButton radioButtonMale;
    private JLabel Gender;
    private JButton buttonSave;
    private JTextArea textArea1;
    private JPanel PanelWest;
private Clinic clinic;

    public PatientFrame(Clinic clinic) {
        this.add(panel1);
         this.clinic=clinic;
        buttonClear.addActionListener(e -> {
            textFieldName.setText("");
            textFieldAddress.setText("");
            textFieldPhone.setText("");
            textFieldMobile.setText("");
            textFieldEmail.setText("");
           textArea1.setText("");
            textFieldMedicalDiagnosis.setText("");
            textFieldBirthDate.setText("");
         
            radioButtonMale.setSelected(false);
            radioButtonFemale.setSelected(false);
        });

        buttonSave.addActionListener(e -> {
            save();
        });
    }
    private void save() {
        try {
            Connection pa = DbConfig.createdConnection();
            PatientDao patientDao = new PatientDao(pa);
            Patient patient = new Patient();
            patient.setName(textFieldName.getText());
            // patient.setGender(radioButtonMale.isSelected() ?
            patient.setAddress(textFieldAddress.getText());
            patient.setPhone(textFieldPhone.getText());
            patient.setMobile(textFieldMobile.getText());
            patient.setEmail(textFieldEmail.getText());
            patient.setDisease(textArea1.getText());
            patient.setMedicalDiagnosis(textFieldMedicalDiagnosis.getText());
            patient.setClinic(clinic);
           if (radioButtonMale.isSelected()) {
               patient.setGender(Patient.Gender.male);
           } else if (radioButtonFemale.isSelected()) {
              patient.setGender(Patient.Gender.female);
           }
              patient.setBirthDate(new Date());

           patientDao.addPatient(patient);

            JOptionPane.showMessageDialog(this, "Patient added successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
