import java.util.*;

//this is the proble we will be solving using decoratoer pattern
/*class Pizza{}
class vegPizza extends Pizza{}
class margheritaPizza extends Pizza{}
class olivePizza extends margeheritaPizza{}
... so on and so forth we can have many types of pizza and we can have many types of toppings as well
*/


//implementing the pizza interface
interface Pizza{
    String getDescription();
    double getCost();
}

//implementing this on different types of pizza
//this is a concrete class that implements the pizza interface
class MargheritaPizza implements Pizza{
    @Override
    public String getDescription(){
        return "Margerita Pizzza";
    }

    @Override
    public double getCost(){
        return 199.0;
    }
}

class PlainPizza implements Pizza{
    @Override
    public String getDescription(){
        return "Plain Pizza";
    }

    @Override
    public double getCost(){
        return 78.4;
    }
}
//now instead of creating a new class for each types of pizza with different topping we
//can uuse decorator pattern that is we can create a topping class
abstract class PizzaDecorator implements Pizza{

    //inside abstract classs we won;t need to implement the methods of interface 
    // beacuse we will be implementing them in the concrete classes. reason for not implementing 
    // them is that we will be using those methods in concrete classes.
    
    protected Pizza pizza;
    public PizzaDecorator(Pizza pizza){
        this.pizza = pizza;
    }

}
class ExtraCheese extends PizzaDecorator{
    //making constructor public so that we can use it in main class
    public ExtraCheese(Pizza pizza){
        super(pizza);//calling constructor of parent class because we are extending the parent
    }

    //need to implements methods of interface too because abstract class is not implementing it
    //question? what if we implement methods in abstract class? then we will not be able 
    // to use those methods in concrete classes because they will be already implemented 
    // in abstract class and we will not be able to override them in concrete classes. 
    // so we need to implement them in concrete classes.

    @Override
    public String getDescription(){
        return pizza.getDescription() + " + Extra Cheese";// this will give error? 
        // because pizza is private in parent class so we can't access it here.
        //now it will be fine check parent class and make pizza protected so that 
        // we can access it in child class.
    }

    @Override
    public double getCost(){
        return pizza.getCost()+12.0;
    }

}

//second concrete class fpor 
class OlivesPizza extends PizzaDecorator{
    public OlivesPizza(Pizza pizza){
        super(pizza);
    }

    @Override
    public String getDescription(){
        return pizza.getDescription() + " + Olivea";
    }

    @Override
    public double getCost(){
        return pizza.getCost() + 12.3;
    }
}

public class Main{
    public static void main(String [] args){
        System.out.println("Decorator Pattern");
        System.out.println();

        Pizza pizza = new ExtraCheese(new OlivesPizza(new MargheritaPizza()));

        System.out.println(pizza.getDescription());
        System.out.println(pizza.getCost());
        //now i need to create 2nd concrete class and it will work fine 

        System.out.println();
        Pizza pizza1 = new ExtraCheese(new OlivesPizza(new PlainPizza()));
        System.out.println(pizza1.getDescription());
        System.out.println(pizza1.getCost());
    }
}
