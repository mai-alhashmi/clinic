import DB.Clinic;
import DB.Patient;

import javax.swing.*;
import java.awt.*;
import javax.swing.*;

public class PatientDetails extends JFrame {
    public PatientDetails() {
        this.setBounds(150, 100, 700, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//       this.getContentPane().add(panel1);
//       this.add(panel1);
        //  this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.yellow);
        JMenu menuManage = new JMenu("Manage");
        menuManage.setForeground(Color.BLUE);
        menuBar.add(menuManage);

        this.setJMenuBar(menuBar);

        JMenuItem menuItemPatientDocuments = new JMenuItem("PatientDocuments");
        menuItemPatientDocuments.setForeground(Color.MAGENTA);
        JMenuItem menuItemPrescriptionManage = new JMenuItem("PrescriptionManage");
        menuItemPrescriptionManage.setForeground(Color.MAGENTA);
        menuManage.add(menuItemPatientDocuments);
        menuManage.addSeparator();

        menuItemPatientDocuments.addActionListener(e -> {
            PatientDocuments patientDocuments = new PatientDocuments();
            patientDocuments.setVisible(true);
            this.setVisible(false);
        });
        menuManage.add(menuItemPrescriptionManage);

        menuItemPrescriptionManage.addActionListener(e -> {
            PrescriptionFrame prescriptionFrame = new PrescriptionFrame();
            prescriptionFrame.setVisible(true);
            this.setVisible(false);
        });

//    }
//    public static void main(String[] args) {
//    PatientDetails detailsFrame = new PatientDetails();
//    detailsFrame.setVisible(true);
//}
    }
}

