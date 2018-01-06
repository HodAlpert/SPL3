package bgu.spl181.net.impl.DataHandling.users;

import java.io.Serializable;
import java.util.List;

public class UsersList implements Serializable {
        private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "UsersList{" +
                "users=" + users +
                '}';
    }
    public void addUser(User user){
        users.add(user);
    }
}
