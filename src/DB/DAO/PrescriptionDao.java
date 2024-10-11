package DB.DAO;
import DB.Clinic;
import  DB.Prescription;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionDao {
    private static Connection connection;

    public PrescriptionDao(Connection connection) {
        this.connection = connection;
    }
    public static void add(Prescription prescription)  throws SQLException {

        String sql = "insert into prescriptions(medicine_name,dosage,duration)values(?,?,?)";
        PreparedStatement st = connection.prepareStatement(sql);

        st.setString(1, prescription.getMedicineName());
        st.setString(2, prescription.getDosage());
        st.setString(3, prescription.getDuration());
        st.executeUpdate();
        st.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "delete from prescriptions where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        st.close();
    }

    public void update(Prescription prescription) throws SQLException {
        String sql = "update prescriptions set medicine_name=?,Dosage=?, Duration=?, users_id=? where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, prescription.getMedicineName());
        st.setString(2, prescription.getDosage());
        st.setString(3, prescription.getDuration());
        //  st.setInt(4,prescription.getUser_id());
        st.executeUpdate();
        st.close();
    }

    public Prescription get(int id) throws SQLException {
        String sql = "select id,medicine_name,dosage,duration,users_id from prescriptions where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, id);
        ResultSet resultSet = st.executeQuery();

        if (resultSet.next()) {
            Prescription pr = new Prescription();
            pr.setId(id);
            pr.setMedicineName(resultSet.getString("Medicine_Name"));
            pr.setDosage(resultSet.getString("Dosage"));
            pr.setDuration(resultSet.getString("Duration"));
            //  pr.setUser(resultSet.getString("User"));

            return pr;
        } else {
            return null;
        }
    }

    public ArrayList<Prescription> get() throws SQLException {
        String sql = "select id,medicine_name,dosage,duration,users_id from prescriptions";
        PreparedStatement st = connection.prepareStatement(sql);
        ArrayList<Prescription> list = new ArrayList<>();
        ResultSet resultSet = st.executeQuery();
        while (resultSet.next()) {
            Prescription pr = new Prescription();
            pr.setId(resultSet.getInt("id"));
            pr.setMedicineName(resultSet.getString("Medicine_Name"));
            pr.setDosage(resultSet.getString("Dosage"));
            pr.setDuration(resultSet.getString("Duration"));
            //  pr.setUserId(resultSet.getString("userId"));

            list.add(pr);
        }
        return list;
    }
}