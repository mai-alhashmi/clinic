package DB.DAO;

import DB.Clinic;
import DB.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientDao {
    private Connection connection;

    public PatientDao(Connection connection) {
        this.connection = connection;
    }

    public void addPatient(Patient patient) throws SQLException {
        String sql = "insert into patients( name, gender,birth_date,address,phone,mobile,email,disease,medical_diagnosis,clinic_id" +
                " ) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement st = connection.prepareStatement(sql);

        st.setString(1, patient.getName());
        st.setString(2, patient.getGender().name());
        java.sql.Date pd=new java.sql.Date(patient.getBirthDate().getTime());
        st.setDate(3, pd);
        st.setString(4, patient.getAddress());
        st.setString(5, patient.getPhone());
        st.setString(6, patient.getMobile());
        st.setString(7, patient.getEmail());
        st.setString(8, patient.getDisease());
        st.setString(9, patient.getMedicalDiagnosis());
        st.setInt(10,patient.getClinic().getId());

        st.executeUpdate();
        st.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "delete from patients where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        st.close();
    }

    public void update(Patient patient) throws SQLException {
        String sql = "update patients set name=?,gender=?,birthDate=?,address=?, phone=?, mobile=?, email=?,disease=?," +
                "medical_diagnosis=?,created_at=?,user_id=?,clinic_id where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, patient.getName());
         // st.setString(2,patient.getGender());
        java.sql.Date pd=new java.sql.Date(patient.getBirthDate().getTime());
        st.setDate(3, pd);
        st.setString(3, patient.getAddress());
        st.setString(4, patient.getPhone());
        st.setString(5, patient.getMobile());
        st.setString(6, patient.getEmail());
        st.setString(9, patient.getDisease());
        st.setString(10, patient.getMedicalDiagnosis());
        st.setDate(11, pd);
        st.setInt(8, patient.getId());
        st.executeUpdate();
        st.close();
    }

    public Patient get(int pid) throws SQLException {
        String sql = "select id,  name, gender,birth_date, address, phone, mobile, email, disease, " +
                "medical_diagnosis, created_at, user_id,clinic_id from patients where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, pid);
        ResultSet resultSet = st.executeQuery();

        if (resultSet.next()) {
            Patient p = new Patient();
            p.setId(resultSet.getInt("id"));
            p.setName(resultSet.getString("name"));
           String g= resultSet.getString("gender");
            if (g.equals("male")) p.setGender(Patient.Gender.male);
            else p.setGender(Patient.Gender.female);
            p.setBirthDate(resultSet.getDate("Birth_date"));
            p.setAddress(resultSet.getString("address"));
            p.setPhone(resultSet.getString("phone"));
            p.setMobile(resultSet.getString("mobile"));
            p.setEmail(resultSet.getString("email"));
            p.setDisease(resultSet.getString("disease"));
            p.setMedicalDiagnosis(resultSet.getString("medical_diagnosis"));
            p.setCreatedAt(resultSet.getDate("Created_at"));

            return p;
        } else {
            return null;
        }
    }

    public ArrayList<Patient> getByClinic(int clinicId) throws SQLException {
        String sql = "select id, name, gender,birth_date, address, phone, mobile, email, disease, medical_diagnosis," +
                " created_at, user_id,clinic_id from patients where clinic_id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1,clinicId);
        ArrayList<Patient> list = new ArrayList<>();
        ResultSet resultSet = st.executeQuery();
        while (resultSet.next()) {
            Patient p = new Patient();
            p.setId(resultSet.getInt("id"));
            p.setName(resultSet.getString("name"));
            if (resultSet.getString("gender").equals("male")){
                p.setGender(Patient.Gender.male);
            }else {
                p.setGender(Patient.Gender.female);
            }

            p.setBirthDate(resultSet.getDate("birth_date"));
            p.setAddress(resultSet.getString("address"));
            p.setPhone(resultSet.getString("phone"));
            p.setMobile(resultSet.getString("mobile"));
            p.setEmail(resultSet.getString("email"));
            p.setDisease(resultSet.getString("disease"));
            p.setMedicalDiagnosis(resultSet.getString("medical_diagnosis"));
            p.setCreatedAt(resultSet.getDate("Created_at"));
           ClinicDao clinicDao=new ClinicDao(connection);
          Clinic clinic= clinicDao.get(clinicId);
          p.setClinic(clinic);
            list.add(p);
        }
        return list;
    }
}