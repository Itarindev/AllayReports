package allayplugins.stompado.manager;

import allayplugins.stompado.connections.model.IDatabase;
import allayplugins.stompado.dao.UserDAO;
import allayplugins.stompado.model.User;
import lombok.val;
import org.apache.commons.lang.RandomStringUtils;

public class UserManager {

    private final IDatabase iDatabase;

    public UserManager(IDatabase iDatabase) {
        this.iDatabase = iDatabase;
    }

    public void insert(String playerName) {
        val user = UserDAO.findUserByName(playerName);
        if (user != null) return;

        val id = RandomStringUtils.random(12, true, true);

        UserDAO.getUsers().add(new User(id, playerName, 0, false));
        iDatabase.executeUpdate("INSERT INTO allayreports_users (id, user, count, reported) VALUES (?,?,?,?)", id, playerName, 0, false);
    }

    public void save(User user) {
        iDatabase.executeUpdate("UPDATE `allayreports_users` SET user = ?, count = ?, reported = ? WHERE id = ?", user.getName(),
                user.getCount(),
                user.isReported(),
                user.getId());
    }
}