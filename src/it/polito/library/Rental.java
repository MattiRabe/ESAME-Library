package it.polito.library;

public class Rental {

    private String idBook;
    private String idReader;
    private String startDate;
    private String endDate;
    private Book book;

    public Rental(String idBook, String idReader, String startDate, Book b) {
        this.idBook = idBook;
        this.idReader = idReader;
        this.startDate = startDate;
        this.endDate=null;
        this.book=b;
    }

    public String getIdBook() {
        return idBook;
    }

    public String getIdReader() {
        return idReader;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEnd(String end){
        this.endDate=end;
    }

    @Override
    public String toString() {
        if(this.endDate==null) return idReader+" "+startDate+" ONGOING";
        return idReader+" "+startDate+" "+endDate;
    }

    //`"1000 GG-MM-AAAA GG-MM-AAAA"`). Se un noleggio è in corso, viene riportata solo la data 
    //d'inizio e la parola `"ONGOING"` al posto della data di fine (nel formato `"1000 GG-MM-AAAA ONGOING"`).

    public String getTitle(){
        return book.getTitle();
    }
    


}
