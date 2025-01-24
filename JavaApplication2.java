
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication2;

/**
 *
 * @author SE22f-133
 */
public class JavaApplication2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        MovieBookingApp app = new MovieBookingApp(); // Shared object

        // Thread for user A booking 10 seats
        Thread userA = new Thread(() -> app.bookSeats("User  A", 10));

        // Thread for user B booking 12 seats
        Thread userB = new Thread(() -> app.bookSeats("User  B", 12));

        userA.start();
        userB.start();

        try {
            userA.join();
            userB.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }
    }
    
}
class InsufficientSeatsException extends Exception {
    public InsufficientSeatsException(String message) {
        super(message);
    }
}

// Immutable class to represent the state of the booking
final class BookingState {
    private final int availableSeats;

    public BookingState(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    // Method to create a new instance with updated available seats
    public BookingState bookSeats(int seatsToBook) throws InsufficientSeatsException {
        if (seatsToBook <= availableSeats) {
            return new BookingState(availableSeats - seatsToBook);
        } else {
            throw new InsufficientSeatsException("Not enough seats available.");
        }
    }
}

class MovieBookingApp {
    private BookingState bookingState = new BookingState(20); // Initial state with 20 available seats

    // Synchronized method to book seats
    public synchronized void bookSeats(String user, int seatsToBook) {
        try {
            // Attempt to book seats and get a new state
            bookingState = bookingState.bookSeats(seatsToBook);
            System.out.println(user + " successfully booked " + seatsToBook + " seats.");
        } catch (InsufficientSeatsException e) {
            System.out.println(user + " booking failed: " + e.getMessage());
        }
        System.out.println("Seats remaining: " + bookingState.getAvailableSeats());
    }
}


    
