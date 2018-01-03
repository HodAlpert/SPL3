package bgu.spl181.net.impl;

public class MovieInUser {
    private String id;
    private String name;

    public MovieInUser(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return
                '\n'+"\t id: " + id + '\n' +
                "\t name: " + name + '\n' +
                '}';
    }
}
