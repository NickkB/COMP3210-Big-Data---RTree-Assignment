import java.util.ArrayList;


//Class for Nodes
public class Node {

    ArrayList<Node> childNodes;
    ArrayList<Point> dataPoints; 
    Node parentNode; 
    Query MBR; 

    public Node(){
        childNodes = new ArrayList<>();
        dataPoints = new ArrayList<>();
        parentNode = null;
        MBR = new Query(new Point(-1, -1), new Point(-1, -1));
    }

    //Calculates the half perimeter 
    public int perimeter(){
        return (MBR.x2() - MBR.x1()) + (MBR.y2() - MBR.y1());
    }
    
    //Checks if node is a leaf node 
    public boolean isLeaf(){
        return childNodes.size() == 0;
    }

    //Checks if node is the root node
    public boolean isRoot(){
        return parentNode == null;
    }

    //Checks if node is overflowing with child nodes or data points
    public boolean isOverFlow(){
        if(isLeaf()){
            if(dataPoints.size() > Main.B){
                return true; 
            }
        }
        else{
            if(childNodes.size() > Main.B){
                return true; 
            }
        }
        return false; 
    }

    //gets MBR's x1 value 
    public int getMBRX1(){
        return MBR.x1();
    }

    //gets MBR's x2 value 
    public int getMBRX2(){
        return MBR.x2();
    }

    //gets MBR's y1 value 
    public int getMBRY1(){
        return MBR.y1();
    }

    //gets MBR's y2 value 
    public int getMBRY2(){
        return MBR.y2();
    }
}
