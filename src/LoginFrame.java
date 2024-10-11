import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DB.DAO.PatientDao;
import DB.DAO.UserDao;
import DB.DbConfig;
import DB.Patient;
import DB.Users;
import DB.Clinic;
import DB.DAO.ClinicDao;
public class LoginFrame extends JFrame {
    private Connection connection;
        public LoginFrame() {
                   this.connection = connection;
            try {
                this.connection = DbConfig.createdConnection();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);

                System.exit(1);
            }
            setTitle("Login");
            setBounds(200, 150, 400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            JPanel panel = new JPanel();
            panel.setBackground(Color.GREEN);
            JLabel labelEmail = new JLabel("Email");
            labelEmail.setForeground(Color.BLUE);
            JLabel labelPassword = new JLabel("Password");
            labelPassword.setForeground(Color.BLUE);
            JLabel labelMessage = new JLabel();
            labelMessage.setForeground(Color.RED);
            JTextField textFieldEmail = new JTextField("A@yaho.com");
            textFieldEmail.setPreferredSize(new Dimension(150, 30));
            JPasswordField textFieldPassword = new JPasswordField("777");
            textFieldPassword.setPreferredSize(new Dimension(150, 30));
            JButton buttonLogin = new JButton("Login");
            buttonLogin.setBackground(Color.GREEN);
            panel.add(textFieldEmail);
            panel.add(textFieldPassword);
            panel.add(buttonLogin);
            c.insets = new Insets(10, 10, 10, 10);
            c.gridx = 0;
            c.gridy = 0;
            add(labelEmail, c);
            c.gridx = 1;
            c.gridy = 0;
            add(textFieldEmail, c);
            c.gridx = 0;
            c.gridy = 1;
            add(labelPassword, c);
            c.gridx = 1;
            c.gridy = 1;
            add(textFieldPassword, c);
            c.gridx = 1;
            c.gridy = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            add(buttonLogin, c);
            c.gridx = 1;
            c.gridy = 3;
            c.fill = GridBagConstraints.HORIZONTAL;
            add(labelMessage, c);
            buttonLogin.addActionListener(e -> {
                String email = textFieldEmail.getText().trim();
                String password = new String(textFieldPassword.getPassword());
                if (email.isEmpty() || password.isEmpty()) {
                    labelMessage.setText("Email and password cannot be empty!");
                    return;
                }
                Users authenticatedUser;
                authenticatedUser = authenticateUser(email, password);
                if (authenticatedUser != null) {
                    labelMessage.setText("");
//                        openClinicPage(authenticatedUser);
                    MainFrame mainFrame = new MainFrame(authenticatedUser);
                    mainFrame.pack();
                    mainFrame.setVisible(true);
                    this.setVisible(false);

                } else {
                    labelMessage.setText("Invalid email or password.");
                }
            });
        }
        private Users authenticateUser(String email, String password) {
            String sql = "SELECT id, name, email, password FROM users WHERE email = ? AND password = ?";

            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setString(1, email);
                st.setString(2, password);
                ResultSet resultSet;
                resultSet = st.executeQuery();

                if (resultSet.next()) {
                    Users user = new Users();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
//                  new ShowClinicFrame(new Clinic());
                    return user;
                }
            } catch (SQLException e) {

            }
            return null;
        }

        public static void main(String[] args) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);

    }
}

