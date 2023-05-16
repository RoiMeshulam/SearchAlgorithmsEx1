import java.io.FileNotFoundException;
import java.awt.Point;
import java.util.LinkedList;


public class Main {
    public static long _start;
    public static long _end;

    public static void output(Algorithms algo){
        _end = System.currentTimeMillis();
        long timeRunning = _end - _start;
        LinkedList<Node> path = algo.getPath();
        for (int i = path.size() - 1; i >=0 ; i--) {
            System.out.println(path.get(i).getDirection());
            System.out.println("-");
        }
        System.out.println("Num: " + algo.getNum());
        System.out.println("Cost: " + path.get(0).getCost());
        System.out.println((timeRunning/1000.0) + " seconds");
    }

    public static void main(String[] args) {
        try {
            _start = System.currentTimeMillis();
            ReadInput reader = new ReadInput("C:\\Users\\rohim\\IdeaProjects\\Ex1-SearchAlgorithms\\src\\input.txt");
            Algorithms algo = new Algorithms(reader);
            output(algo);



        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

    }
}