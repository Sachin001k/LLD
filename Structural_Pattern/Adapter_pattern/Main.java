import java.util.*;

//gateway logic for understanding the adapter pattern
interface paymentGateway{
    void pay(String orderId, double amount);
}

class payUgateway implements paymentGateway{
    @Override
    public void pay(String orderId, double amount){
        System.out.println("Paid ₹" +amount +" using PayU for orderId: "+orderId);
    }
}

class RazorpayApi{
    //this is 3rd party api have differnt signature for payment method so this is the problem
    //we are solving using adapter patthern as we can't use this in checkout 
    //
    public void makePayment(String invoiceId, double amountInRupees){
        System.out.println("Paid ₹" +amountInRupees +" using Razorpay for invoiceId: "+invoiceId);
    }
}

///to make this works we need to adapter class for razorpay api
class Razorpay implements paymentGateway{
    private RazorpayApi razorpayApi;
    
    //constructor to initialize the razorpay api
    public Razorpay(){
        this.razorpayApi = new RazorpayApi();
    }
    @Override
    public void pay(String orderId, double amount){
       
        razorpayApi.makePayment(orderId, amount);
        System.out.println("Paid ₹ " + amount + " using Razorpay for orderId: "+orderId);
    }
}
//making a checkout service for these payment gateways
class checkOutService{
    private paymentGateway paymentGateway; // this is interface reference to payment gateway this means 
    //we can use any payment gateway that implements this interface in this checkout service 
    //more formal definition of this is that we are using dependency inversion principle here as we are depending on abstraction not on concrete implementation
    public checkOutService(paymentGateway paymentGateway){
        this.paymentGateway = paymentGateway;
    }

    public void checkout(String orderId, double totalAmount){
        paymentGateway.pay(orderId, totalAmount);
    }
}
class Main{
    public static void main(String[] args){

        System.out.println("Adapter Pattern");
        //creating an instance of the adapter
        paymentGateway payU = new payUgateway();
        System.out.println("Using PayU gateway");
        //creating an instance of the adapter
        paymentGateway razorpay = new Razorpay();
        System.out.println("Using Razorpay gateway");
        //creating an instance of the checkout service and passing the adapter to it
        checkOutService checkOutService = new checkOutService(razorpay);
        //calling the checkout method of the checkout service
        checkOutService.checkout("12345", 1000.0);
        
    }
}