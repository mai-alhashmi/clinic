package DB.DAO;
import DB.Clinic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClinicDao {
    private Connection connection;

    public ClinicDao(Connection connection) {
        this.connection = connection;
    }
    public  void add(Clinic clinic) throws SQLException {

        String sql ="insert into clinic (id,name, specialty, address, phone, mobile, email, doctor_name) values(?,?,?,?,?,?,?,?)";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1,clinic.getId());
        st.setString(2,clinic.getName());
        st.setString(3,clinic.getSpecialty());
        st.setString(4,clinic.getAddress());
        st.setString(5,clinic.getPhone());
        st.setString(6,clinic.getMobile());
        st.setString(7,clinic.getEmail());
        st.setString(8,clinic.getDoctorName());
        st.executeUpdate();
        st.close();
   }

    public void delete(int id) throws SQLException {
        String sql ="delete from clinic where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1,id);
        st.executeUpdate();
        st.close();
    }

    public  void update(Clinic clinic) throws SQLException {
        String sql ="update clinic set name=?,specialty=?, address=?, phone=?, mobile=?, email=?, doctor_name=? where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1,clinic.getName());
        st.setString(2,clinic.getSpecialty());
        st.setString(3,clinic.getAddress());
        st.setString(4,clinic.getPhone());
        st.setString(5,clinic.getMobile());
        st.setString(6,clinic.getEmail());
        st.setString(7,clinic.getDoctorName());
        st.setInt(8,clinic.getId());
        st.executeUpdate();
        st.close();
    }
//
    public  Clinic get(int id) throws SQLException {
        String sql ="select name, specialty, address, phone, mobile, email, doctor_name from clinic where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1,id);
        ResultSet resultSet = st.executeQuery();

        if (resultSet.next()){
            Clinic c = new Clinic();
            c.setId(id);
            c.setName(resultSet.getString("name"));
            c.setSpecialty(resultSet.getString("specialty"));
            c.setAddress(resultSet.getString("address"));
            c.setPhone(resultSet.getString("phone"));
            c.setMobile(resultSet.getString("mobile"));
            c.setEmail(resultSet.getString("email"));
            c.setDoctorName(resultSet.getString("doctor_name"));
            return  c;
        }else{
            return null;
        }
    }
    public ArrayList<Clinic> get() throws SQLException {
        String sql ="select id ,name, specialty, address, phone, mobile, email, doctor_name from clinic";
        PreparedStatement st = connection.prepareStatement(sql);
        ArrayList<Clinic> list = new ArrayList<>();
        ResultSet resultSet = st.executeQuery();
        while (resultSet.next()){
            Clinic c = new Clinic();
            c.setId(resultSet.getInt("id"));
            c.setName(resultSet.getString("name"));
            c.setSpecialty(resultSet.getString("specialty"));
            c.setAddress(resultSet.getString("address"));
            c.setPhone(resultSet.getString("phone"));
            c.setMobile(resultSet.getString("mobile"));
            c.setEmail(resultSet.getString("email"));
            c.setDoctorName(resultSet.getString("doctor_name"));
            list.add(c);
        }
        return list;
    }
}
