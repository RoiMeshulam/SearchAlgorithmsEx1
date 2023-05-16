import java.awt.Point;

public class Node implements Comparable<Node> {
    private Point position;
    private Node parent;
    private int id;
    private String direction;
    private double cost;
    private double f;
    private int order;
    private Boolean isMarked;


//Constructor

    public Node(Point position) {
        this.position = position;
        this.parent = null;
        this.id = (int)(position.getX() + (ReadInput._boardSize* position.getY()));
        this.direction="";
        this.cost = 0;
        this.f = 0;
        this.order = -1;
        this.isMarked =false;
    }

    public Node(Node other) {
        this.position = other.getPosition();
        this.parent = other.getParent();
        this.id = other.getId();
        this.direction = other.getDirection();
        this.f = other.getF();
        this.cost = other.getCost();
        this.order = other.getOrder();
        this.isMarked = other.isMarked();
    }

    public Node(Node parent, Point position){
        this.position = position;
        this.parent = parent;
        this.id = (int)(position.getX() + (ReadInput._boardSize* position.getY()));
        this.direction=findDirection(parent.getPosition(),position);
        this.cost = 0;
        this.f = 0;
        this.order = -1;
        this.isMarked =false;



    }

    public Boolean isMarked() {
        return isMarked;
    }

    public void setMarked(Boolean marked) {
        isMarked = marked;
    }

    public void setOrder(int order){
        this.order=order;
    }
    public int getOrder() {
        return this.order;
    }

    public Point getPosition() {
        return position;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return this.direction;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public double getF() {
        return this.f;
    }

    public void setF(double f) {
        this.f= f;
    }

    public void setCost(double cost){
        this.cost = cost;
    }

    public double getCost() {
        return this.cost;
    }


    private String findDirection(Point parent, Point neighbor){
        int dx = (int)(parent.getX() - neighbor.getX());
        int dy = (int)(parent.getY() - neighbor.getY());

        if (dx == 0 && dy == 0) {
            // Same point
            return null;
        }

        if (dx > 0) {
            if (dy > 0) {
                return "RD";
            } else if (dy == 0) {
                return "R";
            } else {
                return "RU";
            }
        } else if (dx == 0) {
            if (dy > 0) {
                return "D";
            } else {
                return "U";
            }
        } else {
            if (dy > 0) {
                return "LD";
            } else if (dy == 0) {
                return "L";
            } else {
                return "LU";
            }
        }


    }

    @Override
    public int compareTo(Node other) {
        if(other.getF() == this.f){
            if(Algorithms.isOldFirstFlag){
                return (int)Double.compare(this.order, other.getOrder());
            }
            else{
                return (int)Double.compare(other.getOrder(),this.order);
            }
        }else{
            return (int)Double.compare(this.f, other.getF());
        }



    }



    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Node)) {
            return false;
        }
        Node other = (Node) obj;
        return this.position.getX() == other.getPosition().getX() && this.position.getY() == other.getPosition().getY();
    }

    @Override
    public String toString() {
        return "(" + this.getPosition().getX() + ", " + this.getPosition().getY() + ")";
    }
}
