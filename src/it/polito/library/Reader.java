package it.polito.library;

public class Reader {

    private static final int BASE=1000;
    private static int INCREMENTER=0;

    private String id;
    private String name;
    private String surname;

    public Reader(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.id=Integer.toString(BASE+INCREMENTER++);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    

    

}
