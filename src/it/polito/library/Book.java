package it.polito.library;

public class Book {

    private static final int BASE=1000;
    private static int INCREMENTER=0;

    private String title;
    private String id;
    private Boolean available;

    public Book(String title) {
        this.title = title;
        this.id=Integer.toString(BASE + INCREMENTER++);
        this.available=true;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Boolean isAvailable(){
        return available;
    }

    public void setAvailability(Boolean status){
        this.available=status;
    }



}
