
//Class for query. Contains two points for x1, x2, y1, y2
public class Query {

    Point pointOne; 
    Point pointTwo; 

    public Query(Point pointOne, Point pointTwo){
        this.pointOne = pointOne;
        this.pointTwo = pointTwo; 
    }

    public Query(String x, String y, String _x, String _y){
        this.pointOne = new Point(Integer.parseInt(x), Integer.parseInt(y));
        this.pointTwo = new Point(Integer.parseInt(_x), Integer.parseInt(_y)); 
    }

    public int x1(){
        return pointOne.x;
    }

    public int x2(){
        return pointTwo.x;
    }

    public int y1(){
        return pointOne.y;
    }

    public int y2(){
        return pointTwo.y;
    }

    public void updateX1(int x){
        pointOne.x = x;
    }

    public void updatey1(int y){
        pointOne.y = y;
    }

    public void updateX2(int x){
        pointTwo.x = x;
    }

    public void updateY1(int y){
        pointTwo.y = y;
    }
    
}
