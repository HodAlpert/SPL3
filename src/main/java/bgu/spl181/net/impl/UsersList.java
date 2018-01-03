package bgu.spl181.net.impl;

import java.util.List;

public class UsersList {
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
}
