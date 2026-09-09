
import java.util.*;
//this is thread safe singleton pattern implementation
class JudgeAnalystics{
    private static final JudgeAnalystics judgeAnalystics = new JudgeAnalystics();

    private JudgeAnalystics() {
    }

    public static JudgeAnalystics getInstance(){
        return judgeAnalystics;
    }
}


//lazy loading not thread safe singleton patthern implementation
//lazy loading is done during excution time when the instance is required
//while in eager loading the instance is createed at the time of class loading
class JudgeAnalysticsLazy{
    private static JudgeAnalysticsLazy judgeAnalystics;

    //constructor is private so no one can create instance of this class
    private JudgeAnalysticsLazy(){

    }

    //geting the instance of this class

    public static JudgeAnalysticsLazy getInstance(){
        if(judgeAnalystics == null){
            judgeAnalystics = new JudgeAnalysticsLazy();
        }
        return judgeAnalystics;
    }
}
//synchronized method to make it thread safe and volatile keyword to make it visible to all threads

class JudgeA{
    private static JudgeA judgeA;
    //constructor
    private JudgeA(){

    }
    //synchronized methode
    public static synchronized JudgeA getInstance(){
        if(judgeA == null){
            judgeA = new JudgeA();
        }
        return judgeA;
    }
}

//volatile keyword to make it visible to all threads
class JudgeB{
    private static volatile JudgeB judgeB;
    //constructor
    private JudgeB(){   
    }
    //synchronized methode
    public static JudgeB getInstance(){
        if(judgeB == null){
            synchronized (JudgeB.class) {
                if(judgeB == null){
                    judgeB = new JudgeB();
                }
            }
        }
        return judgeB;
    }
}   

public class Main {

    public static void main(String[] args) {
        System.out.println("Singleton Pattern Example");
        // JudgeAnalystics ja1 = JudgeAnalystics.getRunCount();
        // JudgeAnalytics jad = new JudgeAnalytics();
        JudgeAnalysticsLazy ja1 = JudgeAnalysticsLazy.getInstance();
        JudgeAnalysticsLazy ja2 = JudgeAnalysticsLazy.getInstance();
        System.out.println(ja1);
        System.out.println(ja2);
    }
}