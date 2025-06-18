package allayplugins.stompado.dao;

import allayplugins.stompado.model.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    @Getter
    private static final List<User> users = new ArrayList<>();

    public static User findUserByName(String userName) {
        return users.stream().filter(user -> user.getName().equalsIgnoreCase(userName)).findFirst().orElse(null);
    }
}