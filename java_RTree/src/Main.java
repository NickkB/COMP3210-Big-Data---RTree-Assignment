import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

     
    public static int B = 4;

    public static void main(String[] args) throws Exception {
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Query> queries = new ArrayList<>();
        long sequentialScanTime = 0;
        long rTreeTime = 0; 

        File file = new File(args[1]);
        Scanner scanner = new Scanner(file);
        
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] inputData = line.split("\\s+");
            queries.add(new Query(inputData[0], inputData[2], inputData[1], inputData[3]));
        }
        
        scanner.close();
        file = new File(args[0]);
        scanner = new Scanner(file);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] inputData = line.split("\\s+");
            points.add(new Point(inputData[0], inputData[1], inputData[2]));
        }

        sequentialScanTime = sequentialQuery(points, queries);

        rTreeTime = runRTreeQuery(points, queries);

        System.out.println("R-Tree is: " + sequentialScanTime / rTreeTime + " times faster than sequential query");
        scanner.close();
    }

    //runs the R-Tree query 
    //returns the total time it takes to run R-Tree query 
    public static long runRTreeQuery(ArrayList<Point> points, ArrayList<Query> queries) throws IOException{
        System.out.println("R-Tree -");
        RTree rtree = new RTree();
        System.out.println("Building the R-Tree: Please wait...");

        for(Point elm: points){
            rtree.insert(rtree.root, elm);
        }

        System.out.println("R-Tree construction completed");
        System.out.println("Running R-Tree Query...");

        long startTime = System.nanoTime();
        ArrayList<Integer> results = new ArrayList<>();
        for(Query elm: queries){
            results.add(rtree.query(rtree.root, elm));
        }
        long endTime = System.nanoTime();
        long time = (endTime - startTime);
        long aveTime = time / queries.size();
        System.out.println("Total time for a R-Tree Query is: " + time + " nanoseconds");
        System.out.println("Averge time for a R-Tree Query is: " + aveTime + " nanoseconds");
        System.out.println("R-Tree results: ");
        String output = "";
        for(Integer elm: results){
            
            output += elm + " ";
            
        }
        System.out.print(output);
        System.out.println("\n");
        FileWriter fw = new FileWriter("output.txt",  true);
        PrintWriter out = new PrintWriter(fw);
        out.println("R-Tree -");
        out.println(output);
        out.close();
        return endTime - startTime;
    }


    //Method to run a sequentialQuery 
    //iterates over all points one by one and checks whether the query is covered by the point 
    //returns the total time it takes to run the sequential query
    public static long sequentialQuery(ArrayList<Point> points, ArrayList<Query> queries) throws FileNotFoundException{
        
        System.out.println("\nSequential-scan -");
        long startTime = System.nanoTime();
        ArrayList<Integer> results = new ArrayList<>();
        for(Query q: queries){
            int count = 0; 
            for(Point p: points){
                if((q.x1() <= p.x && p.x <= q.x2()) && (q.y1() <= p.y && p.y <= q.y2())){
                    count++;
                }
            }
            results.add(count);
        }
        long endTime = System.nanoTime();
        long time = (endTime - startTime);
        long aveTime = time / queries.size();
        System.out.println("Total time for a sequential-scan is: " + time + " nanoseconds");
        System.out.println("Averge time for a sequential-scan is: " + aveTime + " nanoseconds");
        System.out.println("Sequential-scan results: ");
        String output = "";
        for(Integer elm: results){
            output += elm + " ";
        }
        System.out.print(output);
        PrintWriter out = new PrintWriter("output.txt");
        out.println("Sequential-scan -");
        out.println(output);
        out.close();
        System.out.println("\n");
        return endTime - startTime;
    }

}
