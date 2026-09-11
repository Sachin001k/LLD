import java.util.*;

class BurgerMeal{
    //these will be there 
    private final String bunType;
    private final String patty;

    //these will be optional
    private final boolean hasCheese;
    private final List<String> toppings;
    private final String side;
    private final String drink;

    //private constructor to enforce builder pattern
    private BurgerMeal(BurgerBuilder builder){
        this.bunType = builder.bunType;
        this.patty = builder.patty;
        this.hasCheese = builder.hasCheese;
        this.toppings = builder.toppings;
        this.side = builder.side;
        this.drink = builder.drink;
    }

    // inner static class for BurgerBuilder
    public static class BurgerBuilder{
        //Required parameters
        private final String bunType;
        private final String patty;

        //optional parameters
        private boolean hasCheese;
        private List<String> toppings;
        private String side;
        private String drink;

        //constuctor for required parameters
        public BurgerBuilder(String bunT, String patty){
            this.bunType = bunT;
            this.patty = patty;
        }
        //constructor for optional parameters
        public BurgerBuilder withCheese(Boolean hasCheese){
            this.hasCheese = hasCheese;
            return this;
        }
        public BurgerBuilder withToppings(List<String> toppings){
            this.toppings = toppings;
            return this;
        }

        public BurgerBuilder withSide(String side){
            this.side = side;
            return this;
        }

        public BurgerBuilder withDrink(String drink){
            this.drink = drink;
            return this;
        }

        public BurgerMeal build(){
            return new BurgerMeal(this);
        }
    }
}

public class Main{
    public static void main(String[] args){
       System.out.println("Builder Pattern");
         //creating a burger meal using builder pattern
        BurgerMeal burgerMeal = new BurgerMeal.BurgerBuilder("Wheat", "veg").build();
        //with cheese
        BurgerMeal burgerMealWithCheese = new BurgerMeal.BurgerBuilder("Wheat", "veg").withCheese(true).build();
    }
}