package DB.DAO;

import DB.Appointments;
import DB.Clinic;
import DB.Patient;
import DB.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentsDao {
    private Connection connection;

    public AppointmentsDao(Connection connection) {
        this.connection = connection;
    }
    public void add(Appointments appointments) throws SQLException {
       // String sql ="insert into appointment (id, appointment_date, clinic_id, patient_id, medical_diagnosis, notes, bill_amount, user_id) values(?,?,?,?,?,?,?,?)";
        String sql ="insert into appointments ( appointment_date,medical_diagnosis, notes, bill_amount,clinic_id,patient_id,user_id) values(?,?,?,?,?,?,?)";
        PreparedStatement st = connection.prepareStatement(sql);
        java.sql. Date pd = new java.sql.Date(appointments.getAppointmentDate().getTime());
        st.setDate(1,pd);
        st.setString(2,appointments.getMedicalDiagnosis());
        st.setString(3,appointments.getNotes());
        st.setDouble(4,appointments.getBillAmount());
        st.setInt(5,appointments.getClinic().getId());
        st.setInt(6,appointments.getPatient().getId());
        st.setInt(7,appointments.getUser().getId());
        st.executeUpdate();
        st.close();

    }
    public void delete(int id) throws SQLException {
        String sql ="delete from appointments where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1,id);
        st.executeUpdate();
        st.close();
    }
    public  void update(Appointments appointments) throws SQLException {
        String sql ="update appointment set  id=?,appointment_date=?, clinic_id=?,patient_id=?,  medical_diagnosis=?," +
                " notes=?, bill_amount=?, user_id=? where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1,appointments.getId());
        java.sql.Date pd=new java.sql.Date(appointments.getId());
        st.setString(2,appointments.getMedicalDiagnosis());
        st.setString(3,appointments.getNotes());
        st.setInt(4,appointments.getId());
        st.setDouble(5,appointments.getBillAmount());

        st.executeUpdate();
        st.close();
    }
    public Appointments get(int id) throws SQLException {
        String sql ="select id, appointment_date, clinic_id, patient_id, medical_diagnosis, notes, bill_amount, user_id from appointments where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1,id);
        ResultSet resultSet = st.executeQuery();

        if (resultSet.next()){
            Appointments a = new Appointments();
            a.setId(id);
            a. setAppointmentDate(resultSet.getDate("appointment_date)"));
            a.setMedicalDiagnosis(resultSet.getString("medical_diagnosis"));
            a.setNotes(resultSet.getString("Notes"));
            a.setBillAmount(resultSet.getDouble(" bill_amount"));

            return  a;
        }else{
            return null;
        }
    }
    public ArrayList<Appointments> get() throws SQLException {
        String sql ="select id, appointment_date, clinic_id, patient_id, medical_diagnosis, notes, bill_amount, user_id from where appointments";
        PreparedStatement st = connection.prepareStatement(sql);
        ArrayList<Appointments> list = new ArrayList<>();
        ResultSet resultSet = st.executeQuery();
        while (resultSet.next()){
            Appointments a = new Appointments();
            a.setId(resultSet.getInt("id"));
           a.setAppointmentDate(resultSet.getDate("appointment_date"));
            a.setMedicalDiagnosis(resultSet.getString("medical_diagnosis"));
            a.setNotes(resultSet.getString("Notes"));
            a.setBillAmount(resultSet.getDouble(" bill_amount"));
            ClinicDao clinicDao=new ClinicDao(connection);
            Clinic clinic= clinicDao.get(resultSet.getInt("clinic_id"));
            a.setClinic(clinic);
            PatientDao patientDao=new PatientDao(connection);
            Patient patient=patientDao.get(resultSet.getInt("patient_id"));
            a.setPatient(patient);
            UserDao userDao=new UserDao(connection);
            Users users=userDao.get(resultSet.getInt("user_id"));
            a.setUser(users);

            list.add(a);
        }
        return list;
    }
    public ArrayList<Appointments> getByClinic(int cid) throws SQLException {
        String sql ="SELECT id, appointment_date, clinic_id, patient_id, medical_diagnosis, notes, bill_amount, user_id FROM appointments where clinic_id=?";

        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1,cid);
        ArrayList<Appointments> list = new ArrayList<>();
        ResultSet resultSet = st.executeQuery();
        while (resultSet.next()){
            Appointments a = new Appointments();
            a.setId(resultSet.getInt("id"));
            a.setAppointmentDate(resultSet.getDate("appointment_date"));
            a.setMedicalDiagnosis(resultSet.getString("medical_diagnosis"));
            a.setNotes(resultSet.getString("notes"));
            a.setBillAmount(resultSet.getDouble("bill_amount"));
            ClinicDao clinicDao=new ClinicDao(connection);
            Clinic clinic= clinicDao.get(cid);
            a.setClinic(clinic);
            PatientDao patientDao=new PatientDao(connection);
            Patient patient=patientDao.get(resultSet.getInt("patient_id"));
            a.setPatient(patient);
            UserDao userDao=new UserDao(connection);
            Users users=userDao.get(resultSet.getInt("user_id"));
            a.setUser(users);
            list.add(a);
        }
        return list;
    }
}
