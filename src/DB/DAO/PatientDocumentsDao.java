
package DB.DAO;

import DB.Clinic;
import DB.Patient;
import DB.PatientDocuments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientDocumentsDao {
    private Connection connection;

    public PatientDocumentsDao(Connection connection) {
        this.connection = connection;
    }

    public void add(PatientDocuments patientDocuments) throws SQLException {
        String sql = "insert into patient_documents( name, file, type, result, comments, patient_id) " +
                "values(?,?,?,?,?,?)";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, patientDocuments.getName()); // name
        st.setString(2, patientDocuments.getFile()); // file
        st.setString(3, patientDocuments.getType()); // type
        st.setString(4, patientDocuments.getResult()); // result
        st.setString(5, patientDocuments.getComments()); // comments
       st.setInt(6, patientDocuments.getPatient().getId()); // patient_id
        st.executeUpdate();
        st.close();
    }
    public void delete(int id) throws SQLException {
        String sql = "delete from patient_documents where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        st.close();
    }
    public void update(PatientDocuments patientDocuments) throws SQLException {
        String sql = "update patient_documents set name=?, file=?, type=?, result=?, comments=?, created_at=?, patient_id=? where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, patientDocuments.getName()); // name
        st.setString(2, patientDocuments.getFile()); // file
      //  st.setString(3, patientDocuments.getType()); // type
        st.setString(4, patientDocuments.getResult()); // result
        st.setString(5, patientDocuments.getComments()); // comments


        st.setInt(8, patientDocuments.getId()); // id
        st.executeUpdate();
        st.close();
    }
    public PatientDocuments get(int id) throws SQLException {
        String sql = "select id, name, file, type, result, comments, created_at, patient_id from patient_documents where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, id);
        ResultSet resultSet = st.executeQuery();

        if (resultSet.next()) {
            PatientDocuments pd = new PatientDocuments();
            pd.setId(resultSet.getInt("id"));
            pd.setName(resultSet.getString("name"));
            pd.setFile(resultSet.getString("file"));
          //  pd.setType(resultSet.getString("type"));
            pd.setResult(resultSet.getString("result"));
            pd.setComments(resultSet.getString("comments"));
            pd.setCreatedAt(resultSet.getDate("created_at"));
          //  pd.setPatientId(resultSet.getInt("patient_id"));
            PatientDao patientDao=new PatientDao(connection);
            Patient patient= patientDao.get(resultSet.getInt("patient_id"));
            pd.setPatient(patient);
            return pd;
        } else {
            return null;
        }
    }
    public ArrayList<PatientDocuments> getAll() throws SQLException {
        String sql = "select id, name, file, type, result, comments, created_at, patient_id from patient_documents";
        PreparedStatement st = connection.prepareStatement(sql);
        ArrayList<PatientDocuments> list = new ArrayList<>();
        ResultSet resultSet = st.executeQuery();

        while (resultSet.next()) {
            PatientDocuments pd = new PatientDocuments();
            pd.setId(resultSet.getInt("id"));
            pd.setName(resultSet.getString("name"));
            pd.setFile(resultSet.getString("file"));
            //pd.setType(resultSet.getString("type"));
            pd.setResult(resultSet.getString("result"));
            pd.setComments(resultSet.getString("comments"));
            pd.setCreatedAt(resultSet.getDate("created_at"));

            PatientDao patientDao=new PatientDao(connection);
            Patient patient= patientDao.get(resultSet.getInt("patient_id"));
            pd.setPatient(patient);
            list.add(pd);

        }
        return list;
    }

    public ArrayList<PatientDocuments> getByPatientID(int patianID) throws SQLException {
        String sql = "select id, name, file, type, result, comments, created_at, patient_id from patient_documents where patient_id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        ArrayList<PatientDocuments> list = new ArrayList<>();
        st.setInt(1,patianID);
        ResultSet resultSet = st.executeQuery();

        while (resultSet.next()) {
            PatientDocuments pd = new PatientDocuments();
            pd.setId(resultSet.getInt("id"));
            pd.setName(resultSet.getString("name"));
            pd.setFile(resultSet.getString("file"));
            pd.setType(resultSet.getString("type"));
            pd.setResult(resultSet.getString("result"));
            pd.setComments(resultSet.getString("comments"));
            pd.setCreatedAt(resultSet.getDate("created_at"));
            //  pd.setPatientId(resultSet.getInt("patient_id"));
            PatientDao patientDao=new PatientDao(connection);
            Patient patient= patientDao.get(resultSet.getInt("patient_id"));
            pd.setPatient(patient);
            list.add(pd);
        }
        return list;
    }
}
