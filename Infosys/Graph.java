import java.util.ArrayList;
import java.util.HashMap;

public class Graph{
    int V;
    int E;
    HashMap<Node,ArrayList<Node>> adj;
    Boolean[] visited;
    Boolean[] onCurrentPath;
    private int max;

    Graph(int V){
        this.V = V;
        this.E = 0;
        this.adj = new HashMap<>(V);
    }

    void addEdge(Node from,Node to){
        ArrayList<Node> tempPointer = adj.getOrDefault(from,new ArrayList<>());
        tempPointer.add(to);
        adj.put(from, tempPointer);
        E++;
    }

    int findMaxWithoutCycle(Node startNode) {
        this.visited = new Boolean[V];
        this.onCurrentPath = new Boolean[V];
        max = 0; 
        dfs(startNode, 0);
        return max;
    }

    public void dfs(Node currentNode, int currentLength) {
        visited[currentNode.index] = true;
        onCurrentPath[currentNode.index] = true;

        max = Math.max(max, currentLength);

        for (Node neighbour : adj.getOrDefault(currentNode, new ArrayList<>())) {
            if (!visited[neighbour.index]) {
                if(neighbour.power > currentNode.power) return;
                dfs(neighbour, currentLength + 1);
                currentNode.power += neighbour.bonus;
            } else if (onCurrentPath[neighbour.index]) {
                // If we encounter a node already on the current path,
                // we have a cycle, but we do not need to do anything here
                // as we're only looking for the longest path without a cycle.
            }
        }

        onCurrentPath[currentNode.index] = false; // Backtrack
    }

    public static void main(String[] args){
        
        int[][] in = new int[][]{ { 78,10}
                                , { 130,0 }};
        Graph G =  new Graph(in.length +1);
        Node start =  new Node(0,123,0);
        int i = 1;
        for(int[] each : in){
            Node end = new Node(i, each[0] ,each[1]);
            G.addEdge(start,end);
            ++i;
        }

        System.out.println(G.findMaxWithoutCycle(start));
        
    }

}
