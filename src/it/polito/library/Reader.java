package it.polito.library;

import java.util.TreeMap;

public class Reader {

    private static final int BASE=1000;
    private static int INCREMENTER=0;

    private String id;
    private String name;
    private String surname;
    private Boolean available;
    private TreeMap<String, Rental> rentals = new TreeMap<>();

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

    public Boolean IsAvailable() {
        return available;
    }

    public void setAvailability(Boolean status){
        this.available=status;
    }

    public TreeMap<String, Rental> getRentals() {
        return rentals;
    }

    public void addRental(Rental r){
        rentals.put(r.getIdBook(), r);
    }

    public int getRentalsSize(){
        return rentals.size();
    }
    

}
