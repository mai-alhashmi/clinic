import DB.Patient;

import javax.swing.*;

public class FormDetails  extends JFrame {
    private JPanel panel1;
    private JTextField textFieldName;
    private JTextField textFieldPhone;
    private JTextField textFieldMobile;
    private JTextField textFieldAddress;
    private JTextField textFieldGender;
    private JLabel patientDetailsLabel;
    private JPanel Male;

    public FormDetails(Patient patient) {
        setBounds(150, 100, 700, 400);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().add(panel1);
        textFieldName.setText(patient.getName());
        textFieldPhone.setText(patient.getPhone());
        textFieldMobile.setText(patient.getMobile());
        textFieldAddress.setText(patient.getAddress());
         textFieldGender.setText( patient.getGender().name());
        JTabbedPane tabbedPane = new JTabbedPane();
        panel1.add(tabbedPane);
        tabbedPane.addTab("Documents", new PatientDocuments(patient));
        tabbedPane.addTab("Prescription", new PrescriptionFrame(patient));
    }

  //  public static void main(String[] args) {

//
//        FormDetails f = new FormDetails(new Patient());
//        f.setVisible(true);

    }



