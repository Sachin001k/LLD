
//example include Logistics service 

//implementing interface
interface Logistics{
    void send();
}

//by air and road
class Air implements Logistics{
    @Override
    public void send() {
        System.out.println("Sending by air");   
    }
}

class Road implements Logistics{
    @Override
    public void send(){
        System.out.println("Road Logistics");
    }
}
//what will happen if we have to add new mode then we need to change the code
//which is not good for open close princple
// so we implements factory pattern
class LogisticsService{
    public void send(String mode){
       Logistics logistics = LogisticsFactory.getLogistics(mode);
       logistics.send();
    }
}

//check the md file so we will implement factory class 
class Train implements Logistics{
    @Override
    public void send(){
        System.out.println("Sending by traun");
    }
}

//instead of changin in. the logistics service class we will create a factory class which will return the object of the class based on the mode
class LogisticsFactory{
    //creating method with interface return type
    public static Logistics getLogistics(String mode){
        if(mode.equals("Air")){
            return new Air();
        }
        else if(mode.equals("Road")){
            return new Road();
        }
        else{
            return new Train();
        }
        //new changes can be added here

    }   
}

public class Main{
    public static void main(String[] args){
        System.out.println("Factory pattern ex");
        LogisticsService logisticsService = new LogisticsService();
        logisticsService.send("Road");
    }
}