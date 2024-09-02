package it.polito.library;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class LibraryManager {
	
	private TreeMap<String, Book> books = new TreeMap<>();
	private TreeMap<String, Integer> numCopies = new TreeMap<>(); 
	private TreeMap<String, Integer> numRentals = new TreeMap<>(); 
	private TreeMap<String, Reader> readers = new TreeMap<>();
	    
    // R1: Readers and Books 
    
    /**
	 * adds a book to the library archive
	 * The method can be invoked multiple times.
	 * If a book with the same title is already present,
	 * it increases the number of copies available for the book
	 * 
	 * @param title the title of the added book
	 * @return the ID of the book added 
	 */
    public String addBook(String title) {
		if(numCopies.containsKey(title)){
			int num = numCopies.get(title);
			numCopies.put(title, num+1);
		}
		else numCopies.put(title, 1);
		Book b = new Book(title);
		books.put(b.getId(), b);
        return b.getId();
    }
    
    /**
	 * Returns the book titles available in the library
	 * sorted alphabetically, each one linked to the
	 * number of copies available for that title.
	 * 
	 * @return a map of the titles liked to the number of available copies
	 */
    public SortedMap<String, Integer> getTitles() {    	
        return numCopies;
    }
    
    /**
	 * Returns the books available in the library
	 * 
	 * @return a set of the titles liked to the number of available copies
	 */
    public Set<String> getBooks() {    	    	
        return books.keySet().stream().collect(Collectors.toSet());
    }
    
    /**
	 * Adds a new reader
	 * 
	 * @param name first name of the reader
	 * @param surname last name of the reader
	 */
    public void addReader(String name, String surname) {
		Reader r = new Reader(name, surname);
		readers.put(r.getId(), r);
    }
    
    
    /**
	 * Returns the reader name associated to a unique reader ID
	 * 
	 * @param readerID the unique reader ID
	 * @return the reader name
	 * @throws LibException if the readerID is not present in the archive
	 */
    public String getReaderName(String readerID) throws LibException {
		if(!readers.containsKey(readerID)) throw new LibException();
        return readers.get(readerID).getName()+ " " +readers.get(readerID).getSurname();
    }    
    
    
    // R2: Rentals Management
    
    
    /**
	 * Retrieves the bookID of a copy of a book if available
	 * 
	 * @param bookTitle the title of the book
	 * @return the unique book ID of a copy of the book or the message "Not available"
	 * @throws LibException  an exception if the book is not present in the archive
	 */
    public String getAvailableBook(String bookTitle) throws LibException {
		if(!numCopies.containsKey(bookTitle))throw new LibException();
        return books.values().stream().filter(b->b.getTitle().equals(bookTitle) && b.isAvailable()==true)
		.sorted(Comparator.comparing(Book::getId)).map(Book::getId).findFirst().orElse("Not available");
    }   

    /**
	 * Starts a rental of a specific book copy for a specific reader
	 * 
	 * @param bookID the unique book ID of the book copy
	 * @param readerID the unique reader ID of the reader
	 * @param startingDate the starting date of the rental
	 * @throws LibException  an exception if the book copy or the reader are not present in the archive,
	 * if the reader is already renting a book, or if the book copy is already rented
	 */
	public void startRental(String bookID, String readerID, String startingDate) throws LibException {
		if(!readers.containsKey(readerID)) throw new LibException();
		if(!books.containsKey(bookID)) throw new LibException();
		if(readers.get(readerID).IsAvailable()==false || books.get(bookID).isAvailable()==false) throw new LibException();
		readers.get(readerID).addRental(new Rental(bookID, readerID, startingDate, books.get(bookID)));

		readers.get(readerID).setAvailability(false);
		books.get(bookID).setAvailability(false);

		if(!numRentals.containsKey(books.get(bookID).getTitle())){
			numRentals.put(books.get(bookID).getTitle(), 1);
		}
		else{
			int num = numRentals.get(books.get(bookID).getTitle());
			numRentals.put(books.get(bookID).getTitle(), num+1);
		}

	}
    
	/**
	 * Ends a rental of a specific book copy for a specific reader
	 * 
	 * @param bookID the unique book ID of the book copy
	 * @param readerID the unique reader ID of the reader
	 * @param endingDate the ending date of the rental
	 * @throws LibException  an exception if the book copy or the reader are not present in the archive,
	 * if the reader is not renting a book, or if the book copy is not rented
	 */
    public void endRental(String bookID, String readerID, String endingDate) throws LibException {
		if(!readers.containsKey(readerID)) throw new LibException();
		if(!books.containsKey(bookID)) throw new LibException();
		readers.get(readerID).getRentals().get(bookID).setEnd(endingDate);
		readers.get(readerID).setAvailability(true);
		books.get(bookID).setAvailability(true);
    }
    
    
   /**
	* Retrieves the list of readers that rented a specific book.
	* It takes a unique book ID as input, and returns the readers' reader IDs and the starting and ending dates of each rental
	* 
	* @param bookID the unique book ID of the book copy
	* @return the map linking reader IDs with rentals starting and ending dates
	* @throws LibException  an exception if the book copy or the reader are not present in the archive,
	* if the reader is not renting a book, or if the book copy is not rented
	*/
    public SortedMap<String, String> getRentals(String bookID) throws LibException {
        return readers.values().stream().flatMap(r->r.getRentals().values().stream())
		.filter(r->r.getIdBook().equals(bookID))
		.collect(Collectors.toMap(Rental::getIdReader, Rental::toString, (p1, p2)->p1, TreeMap::new));
    }
    
    
    // R3: Book Donations
    
    /**
	* Collects books donated to the library.
	* 
	* @param donatedTitles It takes in input book titles in the format "First title,Second title"
	*/
    public void receiveDonation(String donatedTitles) {
		String[] s = donatedTitles.split(",");
		for(String st: s){
			if(numCopies.containsKey(st)){
				int num = numCopies.get(st);
				numCopies.put(st, num+1);
			}
			else numCopies.put(st, 1);
			Book b = new Book(st);
			books.put(b.getId(), b);
		}
    }
    
    // R4: Archive Management

    /**
	* Retrieves all the active rentals.
	* 
	* @return the map linking reader IDs with their active rentals

	*/
    public Map<String, String> getOngoingRentals() {
        return readers.values().stream().flatMap(r->r.getRentals().values().stream())
		.filter(r->r.getEndDate()==null).collect(Collectors.toMap(Rental::getIdReader, Rental::getIdBook));
    }
    
    /**
	* Removes from the archives all book copies, independently of the title, that were never rented.
	* 
	*/
    public void removeBooks() {
		List<String> l =  readers.values().stream().flatMap(r->r.getRentals().values().stream())
		.map(Rental::getIdBook).distinct().collect(Collectors.toList());

		for(String id: books.keySet()){
			if(!l.contains(id)){
				if(books.get(id)!=null) books.remove(id);
				if(numCopies.get(books.get(id).getTitle())!=null)numCopies.remove(books.get(id).getTitle());
			}
		}
    }
    	
    // R5: Stats
    
    /**
	* Finds the reader with the highest number of rentals
	* and returns their unique ID.
	* 
	* @return the uniqueID of the reader with the highest number of rentals
	*/
    public String findBookWorm() {
        Reader r =readers.values().stream().sorted(Comparator.comparing(Reader::getRentalsSize).reversed())
		.findFirst().orElse(null);
		return r.getName()+" "+r.getSurname();
    }
    
    /**
	* Returns the total number of rentals by title. 
	* 
	* @return the map linking a title with the number of rentals
	*/
    public Map<String,Integer> rentalCounts() {
    	return numRentals;
    }

}
