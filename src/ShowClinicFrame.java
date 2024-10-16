import DB.*;
import DB.DAO.AppointmentsDao;
import DB.DAO.PatientDao;
import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.TableColumn;

public class ShowClinicFrame extends JFrame {

    public ShowClinicFrame(Clinic clinic, Users user) {
        this.setBounds(150, 140, 700, 400);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
      //  this.user=user;
        setTitle(clinic.getName() + " - " + clinic.getDoctorName() + " - " + clinic.getSpecialty());
       // JLabel labelUser = new JLabel("Welcome " + user.getName());
       // getContentPane().add(labelUser, BorderLayout.WEST);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame m = new MainFrame(user);
                m.pack();
                m.setVisible(true);
            }
        });

        Font font = new Font("Thoma", Font.PLAIN, 18);
        JPanel panelNorth = new JPanel();
        getContentPane().add(panelNorth, BorderLayout.NORTH);


        JButton buttonAddPatient = new JButton("Add Patient");
        buttonAddPatient.setPreferredSize(new Dimension(200, 100));
        buttonAddPatient.setFont(font);
        buttonAddPatient.setContentAreaFilled(false);

        JButton buttonShowAllPatient = new JButton("Show All Patients");
        buttonShowAllPatient.setPreferredSize(new Dimension(200, 100));
        buttonShowAllPatient.setFont(font);
        buttonShowAllPatient.setContentAreaFilled(false);

        JButton buttonAppointment = new JButton("Appointment");
        buttonAppointment.setPreferredSize(new Dimension(200, 100));
        buttonAppointment.setContentAreaFilled(false);

        JButton buttonAllAppointment = new JButton("All Appointments");
        buttonAllAppointment.setPreferredSize(new Dimension(200, 100));
        buttonAllAppointment.setContentAreaFilled(false);

        panelNorth.add(buttonAddPatient);
        panelNorth.add(buttonShowAllPatient);
        panelNorth.add(buttonAppointment);
        panelNorth.add(buttonAllAppointment);

        PatientFrame panel1 = new PatientFrame(clinic);
        ShowAllPatientPanel panel2 = new ShowAllPatientPanel(clinic);
        AppointmentsFrame panel3 = new AppointmentsFrame(clinic, user);
        ShowAllAppointmentPanel panel4 = new ShowAllAppointmentPanel(clinic);

        CardLayout cardLayout = new CardLayout();
        JPanel panelCenter = new JPanel(cardLayout);
        this.getContentPane().add(panelCenter);
        panelCenter.add(panel1, "addPatient");
        panelCenter.add(panel2, "showAllPatients");
        panelCenter.add(panel3, "appointments");
        panelCenter.add(panel4, "allAppointments");

        buttonAddPatient.addActionListener(e -> {
            cardLayout.show(panelCenter, "addPatient");
        });
        buttonShowAllPatient.addActionListener(e -> {
            panel2.fillTable();
            cardLayout.show(panelCenter, "showAllPatients");

        });
        buttonAppointment.addActionListener(e -> {

            cardLayout.show(panelCenter, "appointments");

        });
        buttonAllAppointment.addActionListener(e -> {
            panel4.fillTable();
            cardLayout.show(panelCenter, "allAppointments");
        });

        this.setVisible(true);
    }

    class ShowAllPatientPanel extends JPanel {
        private DefaultTableModel dtm;
        private JTable table;
        private Clinic clinic;

        public ShowAllPatientPanel(Clinic clinic) {
            this.setLayout(new BorderLayout());
            this.clinic = clinic;
            String[] columns = {"id", "name", "action", "gender", "birth_date", "address", "phone", "mobile", "email", "disease", "medical_diagnosis", "created_at" ,"clinic" };

            dtm = new DefaultTableModel(null, columns);
            table = new JTable(dtm);
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = table.getSelectedRow();
                    String pid =  table.getValueAt(row , 0).toString();
                    Patient patient;
                    try {
                        Connection pa = DbConfig.createdConnection();
                            PatientDao patientDao = new PatientDao(pa);
                        patient = patientDao.get(Integer.parseInt(pid));
                        FormDetails f = new FormDetails(patient);
                        f.setVisible(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            Font font = new Font("Arial", Font.PLAIN, 20);
            table.setFont(font);
            table.setForeground(Color.DARK_GRAY);
            table.getTableHeader().setFont(font);
            table.getTableHeader().setBackground(new Color(100, 227, 212));

            fillTable();
            JScrollPane scrollPane = new JScrollPane(table);
            this.add(scrollPane, BorderLayout.CENTER);

            JButton buttonDelete = new JButton("Delete");
            this.add(buttonDelete, BorderLayout.SOUTH);
            buttonDelete.addActionListener(e -> deleteSelectedPatient());
        }

        private void fillTable() {
            dtm.setRowCount(0);
            try (Connection pa = DbConfig.createdConnection()) {
                PatientDao patientDao = new PatientDao(pa);
                ArrayList<Patient> arrayList = patientDao.getByClinic(clinic.getId());
                for (Patient patient : arrayList) {
                    String[] row = {
                            String.valueOf(patient.getId()),
                            patient.getName(),
                            patient.getGender().name(),
                            patient.getBirthDate().toString(),
                            patient.getAddress(),
                            patient.getPhone(),
                            patient.getMobile(),
                            patient.getEmail(),
                            patient.getDisease(),
                            patient.getMedicalDiagnosis(),
                            patient.getCreatedAt().toString(),
                            patient.getClinic().getName(),
                    };
                    dtm.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "exception");
            }
        }

        private void deleteSelectedPatient() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int patientId = Integer.parseInt((String) table.getValueAt(selectedRow, 0));
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this patients?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection pa = DbConfig.createdConnection()) {
                        PatientDao patientDao = new PatientDao(pa);
                        String sql = "DELETE FROM patients WHERE id = ?";
                        try (PreparedStatement ps = pa.prepareStatement(sql)) {
                            ps.setInt(1, patientId);
                            ps.executeUpdate();
                        }
                        dtm.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(this, "Patient deleted successfully.");
                    } catch (SQLException exception) {
                        JOptionPane.showMessageDialog(this, "An error occurred: " + exception.getMessage());
                    }
                }
            }
        }
    }
    class ShowAllAppointmentPanel extends JPanel {
        private DefaultTableModel dtm;
        private JTable table;
        private Clinic clinic;

        public ShowAllAppointmentPanel(Clinic clinic) {
            setLayout(new BorderLayout());
            setBackground(Color.PINK);
            this.setLayout(new BorderLayout());
            this.clinic = clinic;
            setBackground(Color.YELLOW);
            String[] columns = {"id", " appointment_date", " clinic_id", "patient_id","patient Name", "medical_diagnosis",
                    "notes", "bill_amount", "user"};
            dtm = new DefaultTableModel(null, columns);
            table = new JTable(dtm);

            JButton buttonDelete = new JButton("Delete");
            this.add(buttonDelete, BorderLayout.SOUTH);
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = table.getSelectedRow();
                    String pid =  table.getValueAt(row , 3).toString();
                    Patient patient;
                    try {
                        Connection pa = DbConfig.createdConnection();
                       PatientDao patientDao =new PatientDao(pa);
                        patient = patientDao.get(Integer.parseInt(pid));
                        FormDetails f = new FormDetails(patient);
                        f.setVisible(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            Font font = new Font("Arial", Font.PLAIN, 20);
            table.setFont(font);
            table.setForeground(Color.DARK_GRAY);
            table.getTableHeader().setFont(font);
            table.getTableHeader().setBackground(new Color(100, 227, 212));

            table.getTableHeader().setBackground(new Color(100, 227, 212));

            fillTable();
            JScrollPane scrollPane = new JScrollPane(table);
            this.add(scrollPane, BorderLayout.CENTER);


            this.add(buttonDelete, BorderLayout.SOUTH);
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = table.getSelectedRow();
                    String pid =  table.getValueAt(row , 3).toString();
                    Patient patient;
                    try {
                        Connection cn =DbConfig.createdConnection();
                        PatientDao patientDAO =new   PatientDao(cn);
                        patient = patientDAO.get(Integer.parseInt(pid));
                       FormDetails f = new FormDetails(patient);
                      f.setVisible(true);


                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            });
        }
        private void fillTable() {
            dtm.setRowCount(0);
            Connection a = null;
            try {
                a = DbConfig.createdConnection();

                AppointmentsDao appointmentsDao = new AppointmentsDao(a);
                ArrayList<Appointments> arrayList = appointmentsDao.getByClinic(clinic.getId());
                ArrayList<Appointments> appointments = new ArrayList<>();
                for (Appointments appointment : arrayList) {
                    String[] row = {
                            appointment.getId() + "",
                            appointment.getAppointmentDate().toString(),
                            appointment.getClinic().getName(),
                            appointment.getPatient().getName(),
                            appointment.getMedicalDiagnosis(),
                            appointment.getNotes(),
                            appointment.getBillAmount() + "",
                            appointment.getUser().getName()
                    };
                    dtm.addRow(row);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

}



