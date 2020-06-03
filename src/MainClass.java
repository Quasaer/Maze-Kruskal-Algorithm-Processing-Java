import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainClass extends PApplet {
    ArrayList<Node> nodes = new ArrayList<>();
    int cols;
    int rows;
    int w  = 20;
    //Hash map which stores the sets of nodes which is an array list
    HashMap<Integer, ArrayList<Node>> sets = new HashMap<>();
    //2D array for edges. An edge is treated as two adjacent nodes.
    ArrayList<Node[]> edges = new ArrayList<>();
    public static void main(String[] args){
        String[] processingArgs = {"MySketch"};
        MainClass mySketch = new MainClass();
        PApplet.runSketch(processingArgs, mySketch);
    }

    public void settings(){
        //Declare size of canvas
        size(800,600);
        //Declare background colour
        //Calculate columns and rows based on width and height of canvas
        cols =floor(this.width/w);
        rows =floor(this.height/w);

        //Generate array list of nodes objects
        for(int j = 0; j < rows; j++){
            for(int i = 0; i < cols; i++){
                Node node = new Node(i ,j,this);
                nodes.add(node);
                //Store node as a set in hash map
                ArrayList<Node> subset = new ArrayList<>();
                subset.add(node);
                sets.put(node.nodeIndex,subset);
            }
        }

        //Store edges

        for(int i = 0; i < nodes.size(); i++){
            //Get all valid neighbouring nodes from the current node
            Node[] nodeNeighbours = nodes.get(i).getNeighbours(this);
            //Loop through the array of valid node neighbours
            for(int j = 0; j< nodeNeighbours.length; j++){
                //If the node is not null then, create array for the node edges and add to edges linked list
                if(nodeNeighbours[j] != null){
                    Node[] edgeNodes = new Node[2];
                    edgeNodes[0] = nodes.get(i);
                    edgeNodes[1] = nodeNeighbours[j];
                    edges.add(edgeNodes);
                }
            }
            //Mark current node as visited to reduce overlap.
            nodes.get(i).visited = true;
        }
        //Randomise edge stack
        Collections.shuffle(edges);
    }

    public void draw(){
        background(100);

        //As long as edges Array isn't empty, do algorithm
        if(!edges.isEmpty()){
            //Pop off top of the edges stack
            //Get the first index of the edges 2d array that is not null starting from the end of the array
            int i = edges.size()-1;
            //Highlight edges
            for(Node edgeNode : edges.get(i)){
                edgeNode.highlight = true;
            }

            //Join sets and remove wall
            setUnion(edges.get(i));

            //Remove set from stack
            edges.remove(i);
        }

        for(Node node: nodes){
            node.show(w);
        }
    }


    void setUnion(Node[] edge){
        //Check that the edges are not in the same set.
        //If they are, then do nothing
        if(edge[0].setKey != edge[1].setKey){
            //Store the set sizes that the edge nodes belongs to for comparison
            Integer setSizeA = sets.get(edge[0].setKey).size();
            Integer setSizeB = sets.get(edge[1].setKey).size();
            //Compare the size of the sets
            int setSizeResult = setSizeA.compareTo(setSizeB);
            if(setSizeResult > 0) {
                //Union on setB into setA
                //Temporarily store the set key to be removed as it will be overwritten
                int tempSetKeyToRemove = edge[1].setKey;
                for(Node setNode : sets.get(edge[1].setKey)){
                    //Changes the node set key variable to the new set that it belongs to
                    setNode.setKey = edge[0].setKey;
                    //Add node to the new set
                    sets.get(edge[0].setKey).add(setNode);
                }
                //Remove the set once union has happened
                sets.remove(tempSetKeyToRemove);
            } else if(setSizeResult < 0) {
                //Union on setA into setB
                //Temporarily store the set key to be removed as it will be overwritten
                int tempSetKeyToRemove = edge[0].setKey;
                for(Node setNode : sets.get(edge[0].setKey)){
                    //Changes the node set key variable to the new set that it belongs to
                    setNode.setKey = edge[1].setKey;
                    //Add node to the new set
                    sets.get(edge[1].setKey).add(setNode);
                }
                //Remove the set once union has happened
                sets.remove(tempSetKeyToRemove);

            } else {

                //If the sizes are the same then union based off of which key is larger
                if(edge[0].setKey > edge[1].setKey){
                    //Temporarily store the set key to be removed as it will be overwritten
                    int tempSetKeyToRemove = edge[1].setKey;
                    for(Node setNode : sets.get(edge[1].setKey)){
                        //Changes the node set key variable to the new set that it belongs to
                        setNode.setKey = edge[0].setKey;
                        //Add node to the new set
                        sets.get(edge[0].setKey).add(setNode);
                    }
                    //Remove the set once union has happened
                    sets.remove(tempSetKeyToRemove);

                }else{

                    //Temporarily store the set key to be removed as it will be overwritten
                    int tempSetKeyToRemove = edge[0].setKey;
                    for(Node setNode : sets.get(edge[0].setKey)){
                        //Changes the node set key variable to the new set that it belongs to
                        setNode.setKey = edge[1].setKey;
                        //Add node to the new set
                        sets.get(edge[1].setKey).add(setNode);
                    }
                    //Remove the set once union has happened
                    sets.remove(tempSetKeyToRemove);

                }
            }
            //Once a union has happened, remove wall between them
            edge[0].removeWall(edge[1]);
        }
    }

}
