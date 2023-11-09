package database;

import model.User;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class makes connection with PostgreSQL
 * There are lots functions interacting with Database
 */
public class UserDatabase {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/project";//change localhost to IP of Database for other devices
    private static final String USER = "postgres";
    private static final String PASSWORD = "Sairen1360!";
    public static Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static void insertUser(User user) {
        try (Connection connection = connect()) {
            String query = "INSERT INTO users (username, password,salt) VALUES (?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());

            byte[] byteSalt = SaltHashing.getSalt();
            String salt = SaltHashing.toHex(byteSalt);
            String securePassword = SaltHashing.saltSHA256(SaltHashing.toHex(byteSalt),user.getPassword());
            statement.setString(2, securePassword);
            statement.setString(3,salt);

            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static User selectUser(String username) {
        User user = null;
        try (Connection connection = connect()) {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("username");
                String password = resultSet.getString("password");
                user = new User(name, password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
    public static void updatePassword(String username, String newPassword) {
        try (Connection connection = connect()) {
            String query = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            String salt = getSalt(username);
            String hashedpassword = SaltHashing.saltSHA256(SaltHashing.toHex(SaltHashing.toByteArray(salt)),newPassword);
            statement.setString(1, hashedpassword);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void updateGPS(String username, String gps){
        try (Connection connection = connect()) {
            String query = "UPDATE users SET gps = ? WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gps);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getGPS(String username) {
        try (Connection connection = connect()) {
            String query = "SELECT gps FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("gps");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int getBrute() {
        int count = 0;
        try (Connection connection = connect()) {
            String query = "SELECT COUNT(attack) FROM brute";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return count;
    }
    public static void deleteBrute() {
        try (Connection connection = connect()) {
            String query = "DELETE FROM brute";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void addBrute() {
        try (Connection connection = connect()) {
            String query = "INSERT INTO brute (attack) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String formattedDate = formatter.format(date);

            statement.setString(1, formattedDate);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static String getSalt(String username) {
        try (Connection connection = connect()) {
            String query = "SELECT salt FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("salt");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean checkPasswordDuplicate(String password) {
        try (Connection connection = connect()) {
            String query = "SELECT * FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String storedSalt = resultSet.getString("salt");
                String storedPassword = resultSet.getString("password");

                byte[] byteSalt = SaltHashing.fromHex(storedSalt);
                String hashedPassword = SaltHashing.saltSHA256(SaltHashing.toHex(byteSalt),password);

                if (storedPassword.equals(hashedPassword)) {
                    return true;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void updateLED(int setting) {
        try (Connection connection = connect()) {
            String query = "UPDATE settings SET led = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, setting);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void updateBuzzer(int setting) {
        try (Connection connection = connect()) {
            String query = "UPDATE settings SET buzzer = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, setting);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
