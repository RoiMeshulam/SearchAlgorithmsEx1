import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Collections;
import java.util.ListIterator;



public class Algorithms {

//Variables
    private HashMap<Integer, Node> openList;
    private HashMap<Integer, Node> closeList;
    private ReadInput _algoDetails;
    private  int _num;
    public static boolean isOldFirstFlag;
    private LinkedList<Node> path;

//Constructor
    public Algorithms(ReadInput algoDetails){
        _algoDetails = new ReadInput(algoDetails);
        _num = 1;
        if(_algoDetails.isOldFirst()!= null){
            isOldFirstFlag= _algoDetails.isOldFirst();
        }
        path = new LinkedList<>();
        String algoName = _algoDetails.getAlgoName();
        if(algoName.equals("BFS")){
            BFSR();
        } else if (algoName.equals("DFID")) {
            DFID();
        } else if (algoName.equals("A*")) {
            UCS();
        } else if (algoName.equals("IDA*")) {
            IDA();
        } else if (algoName.equals("DFBnB")) {
            DFBnB();
        }else{
            System.out.println("Invalid algorithm name");
        }

    }
//######################### Algorithms ######################### //
    public boolean BFSR (){
        openList = new HashMap<>();
        closeList = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(_algoDetails.getStartNode());
        openList.put(_algoDetails.getStartNode().getId(), _algoDetails.getStartNode());
        while (!queue.isEmpty()){
            Node n = queue.poll();
            System.out.println("q node" + n);
            closeList.put(n.getId(),n);
            LinkedList<Node> relevantNeighbors = getNeighbors(n);
            System.out.println("neighbors: ");
            for (int i = 0; i < relevantNeighbors.size(); i++) {
                Node g = relevantNeighbors.get(i);
                System.out.println(g);
                if(!closeList.containsKey(g.getId()) && !openList.containsKey(g.getId())){
                    if(g.equals(_algoDetails.getGoalNode())){
                        updatePath(g);
                        System.out.println(_num);;
                        return true;
                    }
                    openList.put(g.getId(),g);
                    queue.offer(g);
                }
            }
            System.out.println("Num: "+ _num);
        }

        return false;
    }



    public String DFID(){
        _num=1;
        for (int depth = 1; depth < Integer.MAX_VALUE ; depth++) {
            openList = new HashMap<>();
            String result = Limited_DFS(_algoDetails.getStartNode(), _algoDetails.getGoalNode(), depth, openList);
            if(result != "cutoff"){
                return result;
            }
        }

        return "fail";
    }

    private String Limited_DFS(Node curr, Node goal, int limit, HashMap<Integer,Node> openList){
        boolean isCutOff= false;
        if(curr.equals(goal)){
            // return the path
            updatePath(curr);
            System.out.println(_num);
            return "sucsses";
        } else if (limit == 0) {
            return "cutoff";
        }else{
            openList.put(curr.getId(),curr);
            LinkedList<Node> relevantNeighbors = getNeighbors(curr);
            for (int i = 0; i < relevantNeighbors.size(); i++) {
                Node g = new Node(relevantNeighbors.get(i));
                if(!openList.containsKey(g.getId())){
                    String result = Limited_DFS(g, goal, limit-1, openList);
                    if(result == "cutoff"){
                        isCutOff=true;
                    } else if (result!= "fail") {
                        return result;
                    }
                }
            }
            openList.remove(curr.getId());
            if(isCutOff){
                return "cutoff";
            }
            else{
                return "fail";
            }
        }

    }

    public boolean UCS(){
        PriorityQueue<Node> queue = new PriorityQueue<>();
        openList = new HashMap<>();
        closeList = new HashMap<>();
        queue.offer(_algoDetails.getStartNode());
        openList.put(_algoDetails.getStartNode().getId(),_algoDetails.getStartNode());
        _algoDetails.getStartNode().setOrder(_num);
        while (!queue.isEmpty()){
            Node front = queue.poll();
            openList.remove(front.getId());
            if(front.equals(_algoDetails.getGoalNode())){
                updatePath(front);
                System.out.println(_num);
                return true;
            }
            closeList.put(front.getId(),front);
            LinkedList<Node> relevantNeighbors = getNeighbors(front);
            for (int i = 0; i < relevantNeighbors.size() ; i++) {
                Node neighbor  = relevantNeighbors.get(i);
                if(!closeList.containsKey(neighbor.getId()) && !openList.containsKey(neighbor.getId())) {
                    queue.offer(neighbor);
                    openList.put(neighbor.getId(),neighbor);
                } else if (openList.containsKey(neighbor.getId())) {
                    Node openListNode = openList.get(neighbor.getId());

                    // to enter open and open
                    if( neighbor.getF() < openListNode.getF()){
                        queue.remove(openListNode);
                        queue.offer(neighbor);
                        openList.put(neighbor.getId(),neighbor);

                    }

                }
            }
        }

        return false;
    }

    public boolean IDA(){
        Stack<Node> stack = new Stack<>();
        openList = new HashMap<>();
        _algoDetails.getStartNode().setF(calculateF(_algoDetails.getStartNode()));
        double t = calculateF(_algoDetails.getStartNode()); // s cost is 0
        while (t != Double.MAX_VALUE){
            _algoDetails.getStartNode().setMarked(false);
            double minF = Double.MAX_VALUE;
            stack.push(_algoDetails.getStartNode());
            openList.put(_algoDetails.getStartNode().getId(),_algoDetails.getStartNode());
            while (!stack.isEmpty()){
                Node n = stack.pop();
                if (n.isMarked()){
                    openList.remove(n.getId());
                }
                else {
                    n.setMarked(true);
                    stack.push(n);
                    LinkedList<Node> relevantNeighbors = getNeighbors(n);
                    for (int i = 0; i < relevantNeighbors.size() ; i++) {
                        Node g = relevantNeighbors.get(i);
                        if (g.getF() > t){
                            minF = Math.min(minF, g.getF());
                            continue;
                        }
                        if(openList.containsKey(g.getId()) && openList.get(g.getId()).isMarked()){
                            continue;
                        }
                        if (openList.containsKey(g.getId()) && !openList.get(g.getId()).isMarked()){
                            if (openList.get(g.getId()).getF() > g.getF()){
                                stack.remove(g);
                                openList.remove(g.getId());
                            }else{
                                continue;
                            }
                        }
                        if (_algoDetails.getGoalNode().equals(g)){
//                            updatePathFromStack(stack);
                            updatePath(g);
                            return true;
                        }
                        System.out.println(g.isMarked());
                        stack.push(g);
                        openList.put(g.getId(),g);
                    }
                }
            }
            t = minF;
        }
        return false;
    }

    public LinkedList<Node> DFBnB(){
        Stack<Node> stack = new Stack<>();
        LinkedList<Node> result = null;
        openList = new HashMap<>();
        _algoDetails.getStartNode().setF(calculateF(_algoDetails.getStartNode()));
        double t = Double.MAX_VALUE;
        stack.push(_algoDetails.getStartNode());
        openList.put(_algoDetails.getStartNode().getId(),_algoDetails.getStartNode());
        while(!stack.isEmpty()){
            Node n = stack.pop();
            if(n.isMarked()){
                openList.remove(n.getId());
            }else{
                n.setMarked(true);
                stack.push(n);
                LinkedList<Node> relevantNeighbors = getNeighbors(n);
                LinkedList<Integer> indexToDelete= new LinkedList<>();
                Collections.sort(relevantNeighbors);
                for (int i = 0; i < relevantNeighbors.size(); i++) {
                    Node g = relevantNeighbors.get(i);
                    if(g.getF()>= t){
                        for (int j = i; j < relevantNeighbors.size() ; j++) {
                            indexToDelete.add(j);
                        }
                        break;
                    } else if (openList.containsKey(g.getId()) && openList.get(g.getId()).isMarked()) {
                        indexToDelete.add(i);
                    } else if (openList.containsKey(g.getId()) && !openList.get(g.getId()).isMarked()) {
                        if(openList.get(g.getId()).getF() <= g.getF()){
                            indexToDelete.add(i);
                        }else{
                            stack.remove(openList.get(g.getId()));
                            openList.remove(g.getId());
                        }
                    } else if (g.equals(_algoDetails.getGoalNode())) {
                        t = g.getF();
                        updatePathFromStack(stack);
                         result = path;
                        for (int j = i; j < relevantNeighbors.size() ; j++) {
                            indexToDelete.add(j);
                        }
                        break;
                    }
                }
                for (int i = relevantNeighbors.size() - 1; i >=0 ; i--) {
                    if(!indexToDelete.contains(i)){
                        stack.push(relevantNeighbors.get(i));
                        openList.put(relevantNeighbors.get(i).getId(),relevantNeighbors.get(i));
                    }
                }
            }
        }




        return result;
    }




//######################### Private Functions ######################### //

    private LinkedList<Point> getAllowedNeighborsPositions(Node n){
        LinkedList<Point> relevantNeighborsPositions = new LinkedList<>();
        int[][] directions;
        // check if it clockwise or counter-clockwise
        if(_algoDetails.isClockWise()){
            directions = new int[][] {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
        }else{
            directions = new int[][] {{0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}};
        }

        // Iterate over the directions according the direction order
        for (int i = 0; i < directions.length; i++) {
            int nx = (int) n.getPosition().getX() + directions[i][0];
            int ny = (int) n.getPosition().getY() + directions[i][1];

            // Check if the neighbor is within the bounds of the board
            if (nx >= 1 && nx <= ReadInput._boardSize && ny >= 1 && ny <= ReadInput._boardSize) {
                Point neighborPos = new Point(nx, ny);
                if (!neighborPos.equals(n.getParent().getPosition()) && _algoDetails.getBoard()[nx - 1][ny - 1] != 'X') {
                    relevantNeighborsPositions.add(neighborPos);
                }
            }
        }
        return relevantNeighborsPositions;
    }





    public LinkedList<Node> getNeighbors(Node n) {
        LinkedList<Node> neighbors = new LinkedList<>();
        int[][] directions;
        // check if it clockwise or counter-clockwise
        if(_algoDetails.isClockWise()){
            directions = new int[][] {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
        }else{
            directions = new int[][] {{0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}};
        }

        // Iterate over the directions according the direction order
        for (int i = 0; i < directions.length; i++) {
            int nx = (int)n.getPosition().getX() + directions[i][0];
            int ny = (int)n.getPosition().getY() + directions[i][1];

            // Check if the neighbor is within the bounds of the board
            if (nx >= 1 && nx <= ReadInput._boardSize && ny >= 1 && ny <= ReadInput._boardSize) {
                Node neighbor = new Node(new Point(nx,ny));
                if(!neighbor.equals(n.getParent()) && _algoDetails.getBoard()[nx-1][ny-1]!= 'X'){
                    if(directions[i][0]== 0 && directions[i][1]== 1){
                        neighbor.setDirection("R");
                    } else if(directions[i][0]== 1 && directions[i][1]== 1){
                        neighbor.setDirection("RD");
                    } else if(directions[i][0]== 1 && directions[i][1]== 0){
                        neighbor.setDirection("D");
                    } else if(directions[i][0]== 1 && directions[i][1]== -1){
                        neighbor.setDirection("LD");
                    } else if(directions[i][0]== 0 && directions[i][1]== -1){
                        neighbor.setDirection("L");
                    } else if(directions[i][0]== -1 && directions[i][1]== -1){
                        neighbor.setDirection("LU");
                    } else if(directions[i][0]== -1 && directions[i][1]== 0){
                        neighbor.setDirection("U");
                    } else if(directions[i][0]== -1 && directions[i][1]== 1){
                        neighbor.setDirection("RU");
                    }
                    neighbor.setParent(n);
                    neighbor.setCost(calculateCost(neighbor));
                    neighbor.setF(calculateF(neighbor));
                    _num++;
                    neighbor.setOrder(_num);
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    public void updatePath(Node n){
        path.add(n);
        if (n.getParent() == null || n.getParent().getParent()== null) {
            return;
        }
        updatePath(n.getParent());
    }

    public void updatePathFromStack(Stack<Node> stack){
        while (!stack.isEmpty()){
            Node front = stack.pop();
            if(front.isMarked()){
                path.add(front);
            }
        }
    }

    private double calculateF(Node n){
        double dx = n.getPosition().getX() - _algoDetails.getGoalNode().getPosition().getX();
        double dy = n.getPosition().getY() - _algoDetails.getGoalNode().getPosition().getY();
//        return n.getCost() + Math.sqrt(dx*dx + dy*dy);
        return n.getCost() +Math.abs(dx) + Math.abs(dy);

    }

    private double calculateCost(Node n){
        if(n.getParent()==null){
            return nodeCost(n);
        }else{
            return n.getParent().getCost() + nodeCost(n);
        }
    }

    private double nodeCost(Node n) {
        char symbol = _algoDetails.getBoard()[(int)n.getPosition().getX() -1][(int)n.getPosition().getY() -1];
        if(symbol =='H' && (n.getDirection().equals("RU") || n.getDirection().equals("LU")|| n.getDirection().equals("RD") || n.getDirection().equals("LD"))){
            return 10;
        }
        switch (symbol) {
            case 'D':
                return 1;
            case 'R':
                return 3;
            case 'H':
            case 'G':
                return 5;
            case 'S':
                return 0;
        }
        return -1;
    }

    public LinkedList<Node> getPath() {
        return path;
    }

    public int getNum() {
        return _num;
    }

    //######################### Public Functions ######################### //



}
