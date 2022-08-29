import java.util.ArrayList;
import java.util.Comparator;

//Class for R-Tree
public class RTree {
    Node root; 

    //Tree constructor. Sets root node
    public RTree(){
        root = new Node(); 
    }

    //Runs the query 
    public int query(Node node, Query query){
        int result = 0;
        if(node.isLeaf()){//if node is leaf
            for(Point elm: node.dataPoints){
                if(isCovered(elm, query)){//check that query is covered by node data points
                    result++;
                } 
            }
            return result;
        }
        else{
            for(Node elm: node.childNodes){//Iterate over child nodes 
                if(isIntersect(elm, query)){//if nodes MBR intersects with query
                    result = result + query(elm, query);
                }
            }
            return result; 
        }   
    }

    //Checks if a query and node's MBR intersect 
    private boolean isIntersect(Node node, Query query){
        int x1Center = (node.MBR.x2() + node.MBR.x1()) / 2;
        int y1Center = (node.MBR.y2() + node.MBR.y1()) / 2;
        int length1 = node.MBR.x2() - node.MBR.x1();
        int width1 = node.MBR.y2() - node.MBR.y1();

        int x2Center = (query.x2() + query.x1()) / 2;
        int y2Center = (query.y2() + query.y1()) / 2;
        int length2 = query.x2() - query.x1();
        int width2 = query.y2() - query.y1();

        boolean test = (Math.abs(x1Center - x2Center) <= (length1 / 2) + (length2 / 2) 
        && Math.abs(y1Center - y2Center) <= (width1 / 2) + (width2 / 2));
        return test;
    }

    //Checks that query is covered by point
    private boolean isCovered(Point point, Query query){
        return (query.x1() <= point.x && point.x <= query.x2()) && (query.y1() <= point.y && point.y <= query.y2()); 
    }

    //Inserts a node 
    public void insert(Node node, Point point){
        if(node.isLeaf()){
            addDataPoint(node, point);
            if(node.isOverFlow()){
                handleOverflow(node);
            }  
        }
        else{
            Node subTreeNode = chooseSubTree(node, point);
            insert(subTreeNode, point);
            updateMBR(subTreeNode);
        }
    }

    //Updates a nodes MBR
    private void updateMBR(Node node){
        ArrayList<Integer> xList = new ArrayList<>(); 
        ArrayList<Integer> yList = new ArrayList<>();
        
        if(node.isLeaf()){
            for(Point elm: node.dataPoints){
                xList.add(elm.x);
                yList.add(elm.y);
            }
        }
        else{
            for(Node elm: node.childNodes){
                xList.add(elm.MBR.x1());
                xList.add(elm.MBR.x2());
                yList.add(elm.MBR.y1());
                yList.add(elm.MBR.y2());
            }
        }
        xList.sort(Comparator.comparing(Integer::intValue));
        yList.sort(Comparator.comparing(Integer::intValue));
        
        Query newMBR = new Query(new Point(xList.get(0), yList.get(0)), new Point(xList.get(xList.size() - 1), yList.get(yList.size() - 1)));
        node.MBR = newMBR;
    }

    //Finds the best sub tree
    private Node chooseSubTree(Node node, Point point){
        if(node.isLeaf()){
            return node;
        }
        else{
            int minIncrease = Integer.MAX_VALUE;
            Node bestChildNode = null;
            for(Node elm: node.childNodes){
                if(minIncrease > perimeterIncrease(elm, point)){
                    minIncrease = perimeterIncrease(elm, point);
                    bestChildNode = elm;
                }
            }
            return bestChildNode;
        }  
    }

    //returns the best perimeter increase 
    //new perimeter - original perimeter = increase of perimeter
    private int perimeterIncrease(Node node, Point point){
        Query originalMBR = node.MBR;
        int increase = (Math.max(originalMBR.x1(), Math.max(originalMBR.x2(), point.x)) - Math.min(originalMBR.x1(), Math.min(originalMBR.x2(), point.x)) +
                        Math.max(originalMBR.y1(), Math.max(originalMBR.y2(), point.y)) - Math.min(originalMBR.y1(), Math.min(originalMBR.y2(), point.y))) - node.perimeter(); 
        return increase;
    }

    //handlles overflow by finding the best split 
    private void handleOverflow(Node node){
        Node[] split = split(node);
        Node node1 = split[0];
        Node node2 = split[1];

        if(node.isRoot()){
            Node newRootNode = new Node();
            addChild(newRootNode, node1);
            addChild(newRootNode, node2);
            root = newRootNode;
            updateMBR(newRootNode);
        }
        else{
            Node newRootNode = node.parentNode;
            newRootNode.childNodes.remove(node);
            addChild(newRootNode, node1);
            addChild(newRootNode, node2);
            if(newRootNode.isOverFlow()){
                handleOverflow(newRootNode);
            }
        }
    }

    //adds child nodes to node
    private void addChild(Node node, Node childNode){
        node.childNodes.add(childNode);
        childNode.parentNode = node;
        if(childNode.MBR.x1() < node.MBR.x1()){
            node.MBR.updateX1(childNode.MBR.x1());
        }
        if(childNode.MBR.x2() > node.MBR.x2()){
            node.MBR.updateX2(childNode.MBR.x2());
        }
        if(childNode.MBR.y1() < node.MBR.y1()){
            node.MBR.updateY1(childNode.MBR.y1());
        }
        if(childNode.MBR.y1() > node.MBR.y1()){
            node.MBR.updateY1(childNode.MBR.y1());
        }
    }

    //adds data points to node 
    private void addDataPoint(Node node, Point point){
        node.dataPoints.add(point);
        if(point.x < node.MBR.x1()){
            node.MBR.updateX1(point.x);
        }
        if(point.x > node.MBR.x2()){
            node.MBR.updateX2(point.x);
        }
        if(point.y < node.MBR.y1()){
            node.MBR.updateY1(point.y);
        }
        if(point.y > node.MBR.y1()){
            node.MBR.updateY1(point.y);
        }
    }

    //finds the best split and returns the new nodes for the split
    private Node[] split(Node node){
        Node bestNode1 = new Node();
        Node bestNode2 = new Node(); 
        int bestPerimeter = Integer.MAX_VALUE;
        if(node.isLeaf()){
            int m = node.dataPoints.size();
            
            ArrayList<ArrayList<Point>> divide = new ArrayList<>(); 
            ArrayList<Point> tempList1 = new ArrayList<>(node.dataPoints);
            ArrayList<Point> tempList2 = new ArrayList<>(node.dataPoints);
            tempList1.sort(Comparator.comparing(Point::getX));
            divide.add(tempList1);
            tempList2= node.dataPoints;
            tempList2.sort(Comparator.comparing(Point::getY));
            divide.add(tempList2);
            for(ArrayList<Point> elm : divide){
                for(int j = (int)Math.ceil(0.4 * Main.B); j < m - Math.ceil(0.4 * Main.B) + 1; j++){
                    Node testNode1 = new Node(); 
                    testNode1.dataPoints.addAll(elm.subList(0, j));
                    updateMBR(testNode1);
                    Node testNode2 = new Node();
                    testNode2.dataPoints.addAll(elm.subList(j, elm.size()));
                    updateMBR(testNode2);
                    if(bestPerimeter > testNode1.perimeter() + testNode2.perimeter()){
                        bestPerimeter = testNode1.perimeter() + testNode2.perimeter();
                        bestNode1 = testNode1;
                        bestNode2 = testNode2; 
                    }
                }
            }
            
        }
        else{     
            int m = node.childNodes.size();
            ArrayList<ArrayList<Node>> divide = new ArrayList<>();
            ArrayList<Node> tempList1 = new ArrayList<>(node.childNodes);
            ArrayList<Node> tempList2 = new ArrayList<>(node.childNodes);
            ArrayList<Node> tempList3 = new ArrayList<>(node.childNodes);
            ArrayList<Node> tempList4 = new ArrayList<>(node.childNodes);
            tempList1.sort(Comparator.comparing(Node::getMBRX1));
            divide.add(tempList1);
            tempList2.sort(Comparator.comparing(Node::getMBRX2));
            divide.add(tempList2);
            tempList3.sort(Comparator.comparing(Node::getMBRY1));
            divide.add(tempList3);
            tempList4.sort(Comparator.comparing(Node::getMBRY2));
            divide.add(tempList4);
            for(ArrayList<Node> elm : divide){
                for(int j = (int)Math.ceil(0.4 * Main.B); j < m - Math.ceil(0.4 * Main.B) + 1; j++){
                    Node testNode1 = new Node(); 
                    testNode1.childNodes.addAll(elm.subList(0, j));
                    updateMBR(testNode1);
                    Node testNode2 = new Node();
                    testNode2.childNodes.addAll(elm.subList(j, elm.size()));
                    updateMBR(testNode2);
                    if(bestPerimeter > testNode1.perimeter() + testNode2.perimeter()){
                        bestPerimeter = testNode1.perimeter() + testNode2.perimeter();
                        bestNode1 = testNode1;
                        bestNode2 = testNode2; 
                    }
                }
            }
        }
        for(Node elm: bestNode1.childNodes){
            elm.parentNode = bestNode1;
        }
        for(Node elm: bestNode2.childNodes){
            elm.parentNode = bestNode2;
        }
        Node[] bestSplit = {bestNode1, bestNode2};
        return bestSplit;  
    }

}
