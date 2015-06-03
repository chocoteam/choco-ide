import org.chocosolver.solver.Solver;
import java.util.Date;

public class Main {
    public static void main(String[] args){
        System.out.println("Debut du script");
        System.out.println(new Date());
        Solver solver = new Solver();
        System.out.println(solver);
    }
}
