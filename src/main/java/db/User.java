package db;

public class User {
    private int id;
    private String name;
    private String dateandtime;
    private String message;


    public User(String name, String dateandtime) {
        this.name = name;
        this.dateandtime = dateandtime;
//        this.message = message;
    }

    public User(String name, String message, String dateandtime) {
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

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
