import processing.core.PApplet;

public class Node {
    private PApplet sketch;

    //Pixel position. Not true x and y position of node.
    int i;
    int j;

    //Boolean array to activate and de-activate walls on node.
    //North, South, East and West
    boolean[] walls = {true,true,true,true};

    //Visited to generate edges. Default set to false.
    boolean visited = false;

    //Highlight node when generating maze
    boolean highlight = false;

    //Set key stores the key for which set the node belongs to
    int setKey;

    //Constant value for the node index for the nodes array so that this value doesn't have to be constantly re calculated
    final int nodeIndex;

    Node( int i, int j, MainClass main){
        this.i = i;
        this.j = j;
        this.sketch = main;
        this.nodeIndex = nodeIndex(this.i,this.j, main.cols, main.rows);
        //Initial value of the setKey will be the same as the node index
        this.setKey = nodeIndex;
    }

    void show(int len){
        //Extract x and y position from the length of the nodes.
        int x = i * len;
        int y = j * len;
        //Check if the node should be highlighted
        if(highlight){
            sketch.noStroke();
            sketch.fill(255);
            sketch.rect(x,y,len,len);
        }
        sketch.stroke(0);
        sketch.noFill();
        //north wall - draw wall when true
        if(this.walls[0]){
            sketch.line(x, y, x + len, y);
        }
        //south wall
        if(this.walls[1]){
            sketch.line(x + len, y + len, x , y + len);

        }
        //west wall
        if(this.walls[2]){
            sketch.line(x , y + len, x , y);
        }
        //east wall
        if(this.walls[3]){
            sketch.line(x + len, y , x + len, y + len);
        }
    }


    Node[] getNeighbours(MainClass main){
        //Return array of valid node neighbours
        //Array size will at maximum be 4
        Node[] neighbours = new Node[4];
        Node top, bottom, left, right;
        //these are the cell neighbours
        //Calculate node index for array position of the neighbour node
        // will return -1 if the node index is out of bounds
        int nodeIndexNum = nodeIndex(this.i, this.j -1, main.cols, main.rows);
        if(nodeIndexNum != -1){
            top = main.nodes.get(nodeIndexNum);
            //Check that node has not been visited.
            //Add to the return array of valid neighbours
            if(!top.visited){
                neighbours[0] = top;
            }
        }

        nodeIndexNum = nodeIndex(this.i, this.j +1, main.cols, main.rows);
        if(nodeIndexNum != -1){
            bottom = main.nodes.get(nodeIndexNum);
            //Check that node has not been visited.
            //Add to the return array of valid neighbours
            if(!bottom.visited){
                neighbours[1] = bottom;
            }
        }

        nodeIndexNum = nodeIndex(this.i -1, this.j, main.cols, main.rows);
        if(nodeIndexNum != -1){
            left = main.nodes.get(nodeIndexNum);
            //Check that node has not been visited.
            //Add to the return array of valid neighbours
            if(!left.visited){
                neighbours[2] = left;
            }
        }

        nodeIndexNum = nodeIndex(this.i +1, this.j, main.cols, main.rows);
        if(nodeIndexNum != -1){
            right = main.nodes.get(nodeIndexNum);
            //Check that node has not been visited.
            //Add to the return array of valid neighbours
            if(!right.visited){
                neighbours[3] = right;
            }
        }

        return neighbours;
    }

    void removeWall(Node adjacentNode){
        // left or right for the difference between the current cell and next cell
        int x = this.i - adjacentNode.i;

        if(x == 1){
            this.walls[2] = false;
            adjacentNode.walls[3] = false;
        } else if(x == -1){
            this.walls[3] = false;
            adjacentNode.walls[2] = false;
        }

        // above or below for the difference between the current cell and next cell
        int y = this.j - adjacentNode.j;

        if(y == 1){
            this.walls[0] = false;
            adjacentNode.walls[1] = false;
        } else if(y == -1){
            this.walls[1] = false;
            adjacentNode.walls[0] = false;
        }
    }

    int nodeIndex(int i, int j, int cols, int rows){
        //validation for edge cases will return a null index
        if (i < 0 || j < 0 || i > cols-1 || j > rows -1){
            return -1;
        }
        return i + j * cols;
    }

}
