package model;

public class User {
    private String name;
    private String password;
    private String gps;

    public User(String name,String password){
        this.name = name;
        this.password = password;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getGps() {
        return gps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }
}
