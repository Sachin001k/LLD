import java.util.*;
//This is the problem we will be solving using facade pattern
class PaymentService{
    public void makePayment(String accountId, double amount){
        System.out.println("Paid ₹ "+amount+" on account no. "+accountId);
        System.out.println();
    }
}

class NotificationService{
    public void sendBookingConfirmation(String emailId){
        System.out.println("Ticket booking is confirmed for email : "+emailId);
    }
}

class SeatReservation{
    public void reserveSeat(String seatNumber, String movieId){
        System.out.println("Your seat number is: "+seatNumber+ " for movieId "+movieId);
        System.out.println();
    }
}

class LoyaltyPoints{
    public void addPoints(String accountId, int points){
        System.out.println("Add points on account Id "+accountId+ " with points " + points);
    }
}

class TicketService{
    public void generateTicket(String movieId, String seatNumber){
        System.out.println("Your movie is "+movieId+" and your seat Number is "+seatNumber);
    }
}

class MovieBookingFacade{
    private PaymentService paymentService;
    private NotificationService notificationService;
    private SeatReservation seatReservation;
    private LoyaltyPoints loyaltyPoints;
    private TicketService ticketService;

    //constructor those details users have to fill
    public MovieBookingFacade(){
        this.paymentService = new PaymentService();
        this.notificationService = new NotificationService();
        this.seatReservation = new SeatReservation();
        this.loyaltyPoints = new LoyaltyPoints();
        this.ticketService = new TicketService();
    }

    //function to access these
    public void bookMovieTickets(String accountId, String movieId, double amount, int points, String email, String seatNumber){
        paymentService.makePayment(accountId, amount);
        notificationService.sendBookingConfirmation(email);
        seatReservation.reserveSeat(seatNumber, movieId);
        loyaltyPoints.addPoints(accountId, points);
        ticketService.generateTicket(movieId, seatNumber);
        System.out.println();
        System.out.println("Congrates booking is successful");
        
    }
}

public class Main{
    public static void main(String[] args){
        System.out.println("Facade Pattern");

        //we are creating too many classes 
        //user have to write in here it's headache so instead we write a facade class
        MovieBookingFacade movieBookingFacade = new MovieBookingFacade();
        movieBookingFacade.bookMovieTickets("123", "Old Gaurd", 345.23, 24, "abs@gmail.com", "A23");
    }
}