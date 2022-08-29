public class Point {

    int x; 
    int y; 
    String id;

    //point constructor 
    public Point(String id, String x, String y){
        this.id = id;
        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
    }

    //point constructor 
    public Point(int x, int y){
        this.x = x;
        this.y = y; 
    }
    
    //return x value 
    public int getX(){
        return x;
    }

    //returns y value
    public int getY(){
        return y;
    }

    @Override
    public String toString(){
        return "id= " + id +  "  x= " + x + "  y= " + y; 
    }
}
