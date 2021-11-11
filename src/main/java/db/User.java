package db;

public class User {

    private final String name;
    private final String dateAndTime;


    public User(String name, String dateAndTime) {
        this.name = name;
        this.dateAndTime = dateAndTime;
    }

    public String getName() {
        return name;
    }

    public String getDateandTime() {
        return dateAndTime;
    }


}
