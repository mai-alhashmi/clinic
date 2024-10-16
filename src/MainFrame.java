import DB.*;
import DB.DAO.ClinicDao;
import DB.DAO.PatientDao;
import DB.PatientDocuments;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    public MainFrame(Users user) {
        setTitle("clinic program");
      //  setBounds(300,150,500,300);
        setLocation(300,150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar=new JMenuBar();
        menuBar.setBackground(Color.ORANGE);
        JMenu menuManage=new JMenu("Manage");
        menuManage.setForeground(Color.BLACK);
        menuBar.add(menuManage);
        this.setJMenuBar(menuBar);
        JMenuItem menuItemClinic=new JMenuItem("Manage Clinic");
        menuItemClinic.setForeground(Color.BLUE);
        JMenuItem menuItemUser=new JMenuItem("Manage User");
        menuItemUser.setForeground(Color.BLUE);
        JMenuItem menuItemPatientDocuments=new JMenuItem("PatientDocuments");
    //   menuItemPatientDocuments.setForeground(Color.BLUE);
     //   JMenuItem menuItemPrescriptionManage=new JMenuItem("PrescriptionManage");
    //   menuItemPrescriptionManage.setForeground(Color.BLUE);
    //    JMenuItem menuItemPatientDetails=new JMenuItem("menuItemPatientDetails");
     //  menuItemPatientDetails.setForeground(Color.BLUE);
        menuManage.add(menuItemClinic);
        menuManage.addSeparator();
        menuManage.add(menuItemUser);
        menuManage.addSeparator();
      //  menuManage.add(menuItemPatientDocuments);
      //  menuManage.addSeparator();
      //  menuManage.add(menuItemPrescriptionManage);
      //  menuManage.addSeparator();
      //  menuManage.add(menuItemPatientDetails);
       // menuItemPatientDetails.addActionListener(e -> {
           // PatientDetails detailsFrame = new PatientDetails();
           // detailsFrame.setVisible(true);
            this.setVisible(false);
     //   });

//        menuItemPrescriptionManage .addActionListener(e -> {
//            PrescriptionFrame prescriptionFrame=new PrescriptionFrame(new Patient());
//            prescriptionFrame.setVisible(true);
//            this.setVisible(false);
//        });
//        menuItemPatientDocuments.addActionListener(e -> {
//            PatientDocuments patientDocuments=new PatientDocuments();
//            patientDocuments.setVisible(true);
//            this.setVisible(false);
//        });
        menuItemUser.addActionListener(e -> {
            UserFrame userFrame=new UserFrame();
            userFrame.setVisible(true);
            this.setVisible(false);
        });
        menuItemClinic.addActionListener(e -> {
            ClinicFrame clinicFrame=new ClinicFrame(user);
             clinicFrame.setVisible(true);
            this.setVisible(false);
        });
        JLabel labelUser = new JLabel("Welcome " + user.getName());
        labelUser.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(labelUser, BorderLayout.NORTH);
         labelUser.setForeground(Color.MAGENTA);
      //  JLabel labelUser = new JLabel("Welcome " + user.getName());
//        getContentPane().add(labelUser , BorderLayout.NORTH);
//      JPanel panelBorderLayout=new JPanel(labelUser.getLayout());
    JPanel panelFlow=new JPanel();
    getContentPane().add(panelFlow);

        try {
           Connection cn = DbConfig.createdConnection();
            ClinicDao clinicDao=new ClinicDao(cn);
            ArrayList<Clinic> arrayList = clinicDao.get();
            for (Clinic clinic:arrayList){
               JButton button=new JButton("<html><cnter>"+clinic.getName()+"<br><br>"+clinic.getDoctorName());
               button.setPreferredSize(new Dimension(200,200));

               panelFlow.add(button);
               button.addActionListener(e -> {
                   new ShowClinicFrame(clinic ,user).setVisible(true);
                   this.setVisible(false);
               });
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}

