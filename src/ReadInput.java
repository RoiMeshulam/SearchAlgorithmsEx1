import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.Point;

 public class ReadInput {
  private String _algoName;
  private boolean _isClockWise;
  private Boolean _isOldFirst ;
  private boolean _withTime;
  private boolean _withOpen;
  public static int _boardSize;
  private Node _start;
  private Node _goal;
  private char[][] _board;


  public ReadInput(ReadInput algoDetails){
   _algoName = algoDetails.getAlgoName();
   _isClockWise= algoDetails.isClockWise();
   _isOldFirst = algoDetails.isOldFirst();
   _withTime = algoDetails.isWithTime();
   _withOpen = algoDetails.isWithOpen();
   _boardSize = algoDetails.getBoardSize();
   _start = algoDetails.getStartNode();
   _goal = algoDetails.getGoalNode();
   _board = algoDetails.getBoard();

  }


  public ReadInput(String path) throws FileNotFoundException {
   File inputFile = new File(path);
   Scanner scanner = new Scanner(inputFile);

   _algoName = scanner.nextLine();
   String temp = scanner.nextLine();
   String[] order = temp.split(" ");
   if(order[0].equals("clockwise")){
     _isClockWise=true;
   }
   else{
    _isClockWise= false;
   }
   if(order.length==2){
    if(order[1].equals("old-first")){
     _isOldFirst=true;
    }
    else{
     _isOldFirst= false;
    }
   }
   else{
     _isOldFirst=null;

   }
   temp = scanner.nextLine();
   if(temp.equals("with time")){
    _withTime=true;
   }
   else{
    _withTime= false;
   }
   temp = scanner.nextLine();
   if(temp.equals("with open")){
    _withOpen=true;
   }
   else{
    _withOpen= false;
   }
   _boardSize = Integer.parseInt(scanner.nextLine());
   temp = scanner.nextLine();
   String[] points = temp.split(",");
   // parse the x and y coordinates of the start and end points
   int startX = Integer.parseInt(points[0].substring(1));
   int startY = Integer.parseInt(points[1].substring(0, points[1].indexOf(')')));
   int endX = Integer.parseInt(points[2].substring(1));
   int endY = Integer.parseInt(points[3].substring(0, points[3].indexOf(')')));

   // create the Point objects for the start and end points
   Point start = new Point(startX, startY);
   Point end = new Point(endX, endY);
   _start = new Node(start);
   _goal = new Node(end);

   _board = new char[_boardSize][_boardSize];
   for (int i = 0; i < _boardSize; i++) {
    String line = scanner.nextLine();
    for (int j = 0; j < _boardSize; j++) {
     _board[i][j] = line.charAt(j);
    }
   }

   scanner.close();
  }

  public String getAlgoName() {
   return _algoName;
  }

  public boolean isClockWise() {
   return _isClockWise;
  }

  public Boolean isOldFirst() {
   return _isOldFirst;
  }

  public boolean isWithTime() {
   return _withTime;
  }

  public boolean isWithOpen() {
   return _withOpen;
  }

  public int getBoardSize() {
   return _boardSize;
  }

  public Node getStartNode() {
   return _start;
  }

  public Node getGoalNode() {
   return _goal;
  }

  public char[][] getBoard() {
   return _board;
  }






}
