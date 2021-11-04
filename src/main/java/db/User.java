package db;

import java.sql.Time;

public class User {
    private int id;
    private String name;
    private String message;
    private Time dateandtime;

     public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String name) {
        this.name = name;
    }

    public User(int id, String name, String message, Time dateandtime) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.dateandtime = dateandtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Time getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(Time dateandtime) {
        this.dateandtime = dateandtime;
    }


}
