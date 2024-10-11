import DB.DAO.PatientDao;
import DB.DAO.PatientDocumentsDao;
import DB.DbConfig;
import DB.Patient;
import DB.Clinic;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class PatientDocuments extends JFrame {
    private JPanel panel1;
    private JPanel PanelNorth;
    private JTextField textFieldName;
    private JTextField textFieldFile;
    private JTextField textFieldResult;
    private JTextField textFieldComments;
    private JComboBox comboBox1;
    private JPanel PanelWest;
    private JButton buttonSave;
    private JButton buttonClear;
    private JButton ButtonDelete;
    private JLabel DocumentManage;
    private JTextField textFieldCreatesAt;
    private JTextField textFieldPatientId;
    private JLabel LabelName;
    private JLabel LabelFile;
    private JLabel LabelResult;
    private DefaultTableModel dtm;
    private JTable table;

    public PatientDocuments() {
        this.setBounds(150, 100, 700, 500);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.getContentPane().add(panel1);
        this.add(panel1);

        //id, name, file, type, result, comments, created_at, patient_id

        String[] columns = {"id", "name", "file", "type", "result", "comments", " created_at", " patient_id"};
        dtm = new DefaultTableModel(null, columns);
        table = new JTable(dtm);
        Font font = new Font("Arial", Font.PLAIN, 20);
        table.setFont(font);
        table.setForeground(Color.DARK_GRAY);
        table.getTableHeader().setFont(font);
        table.getTableHeader().setBackground(new Color(100, 227, 212));
        fillTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel1.add(scrollPane);
        buttonSave.addActionListener(e -> {
            save();
        });
    }
        private void save() {
            try {

                Connection connection = DbConfig.createdConnection();
               PatientDocumentsDao patientDocumentsDao = new PatientDocumentsDao(connection);

                DB.PatientDocuments patientDocuments = new DB.PatientDocuments();
                patientDocuments.setName(textFieldName.getText());
                patientDocuments.setFile(textFieldFile.getText());
                patientDocuments.setResult(textFieldResult.getText());
                patientDocuments.setComments(textFieldComments.getText());
                patientDocuments.setCreatedAt(new Date());
                patientDocuments.setType(comboBox1.getSelectedItem().toString());
                PatientDao patientDao=new PatientDao(connection);
                Patient patient= patientDao.get(Integer.parseInt(textFieldPatientId.getText()));
                patientDocuments.setPatient(patient);
                patientDocumentsDao.add(patientDocuments);
                fillTable();

                JOptionPane.showMessageDialog(this, "Patient Document added successfully");
            } catch (SQLException e) {
e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Patient Document added  Error");
            }
        }
    private void  fillTable() {
                dtm.setRowCount(0);
                try {
                    Connection pd = DbConfig.createdConnection();
                    PatientDocumentsDao patientDocumentsDao = new PatientDocumentsDao(pd);
                    ArrayList<DB.PatientDocuments> documentsList = patientDocumentsDao.getAll();

                    for (DB.PatientDocuments document : documentsList) {
                        String[] row = {
                              //  document.getId();
                                document.getName(),
                                document.getFile(),
                              //  document.getType(),
                                document.getResult(),
                                document.getComments(),
                                document.getCreatedAt().toString(),

                        };
                        dtm.addRow(row);
                    }
                    pd.close();
                } catch (SQLException e) {

                    JOptionPane.showMessageDialog(this, "");
                }
                buttonClear.addActionListener(e -> {

                    textFieldName.setText("");
                    textFieldFile.setText("");
                   textFieldResult.setText("");
                    textFieldComments.setText("");
                   textFieldCreatesAt.setText("");
                   textFieldPatientId.setText("");
                });
                table.getModel().addTableModelListener(e -> {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    if (row >= 0 && row < table.getRowCount() && col >= 0 && col < table.getColumnCount()) {
                        String value = (String) table.getValueAt(row, col);
                        String patientDocuments = (String) table.getValueAt(row, 0);
                        try {
                            Connection pd = DbConfig.createdConnection();
                           PatientDocumentsDao patientDocumentsDao = new PatientDocumentsDao(pd);
                           pd.close();
                        } catch (SQLException exception) {
                            throw new RuntimeException();
                        }
                    }
                });
        }
    public static void main(String[] args) {
        PatientDocuments patientDocuments=new PatientDocuments();
       patientDocuments.setVisible(true);
    }

    public Component getPanel() {
        return null;
    }
}

