package DB.DAO;

import DB.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public void add(Users user) throws SQLException {
        String sql = "INSERT INTO users (id, name, email, password) VALUES (?, ?, ?, ?)";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, user.getId());
        st.setString(2, user.getName());
        st.setString(3, user.getEmail());  // إعداد البريد الإلكتروني
        st.setString(4, user.getPassword());  // إعداد كلمة المرور
        st.executeUpdate();
        st.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "delete from users where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        st.close();
    }

    public void update(Users user) throws SQLException {
        String sql = "UPDATE users SET id = ?, name = ?, email = ?, password = ? WHERE id = ?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, user.getId());
        st.setString(2, user.getName());
        st.setString(3, user.getEmail());
        st.setString(4, user.getPassword());

        st.executeUpdate();
        st.close();
    }

    public Users get(int id) throws SQLException {
        String sql = "select id, name,  email, password from users where id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, id);
        ResultSet resultSet = st.executeQuery();

        if (resultSet.next()) {
            Users u = new Users();
            u.setId(id);
            u.setName(resultSet.getString("name"));
            //  u.setRole(resultSet.getString("Role"));

            u.setEmail(resultSet.getString("email"));
            u.setPassword(resultSet.getString("password"));
            return u;
        } else {
            return null;
        }
    }

    public ArrayList<Users> get() throws SQLException {
        String sql = "SELECT id, name, email, password FROM users";
        PreparedStatement st = connection.prepareStatement(sql);
        ArrayList<Users> list = new ArrayList<>();
        ResultSet resultSet = st.executeQuery();
        while (resultSet.next()) {
            Users u = new Users();
            u.setId(resultSet.getInt("id"));
            u.setName(resultSet.getString("name"));
            //  u.setRole(resultSet.getString("Role"));
            u.setEmail(resultSet.getString("email"));
            u.setPassword(resultSet.getString("Password"));
            list.add(u);
        }
        return list;
    }

}

