package allayplugins.stompado.connections.transform;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.connections.model.IDatabase;
import allayplugins.stompado.dao.UserDAO;
import allayplugins.stompado.model.User;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTransform {

    protected AllayReports main;

    private final IDatabase iDatabase;

    public UserTransform(AllayReports main) {
        this.main = main;

        iDatabase = main.getIDatabase();
    }

    public User userTransform(ResultSet rs) throws SQLException {
        val id = rs.getString("id");
        val user = rs.getString("user");
        val count = rs.getInt("count");
        val isReported = rs.getBoolean("reported");

        return new User(id, user, count, isReported);
    }

    public void loadUsers() {
        try (val ps = iDatabase.getConnection().prepareStatement("SELECT * FROM `allayreports_users`")) {
            val rs = ps.executeQuery();

            while (rs.next()) {
                val user = userTransform(rs);
                if (user == null)
                    continue;

                UserDAO.getUsers().add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}