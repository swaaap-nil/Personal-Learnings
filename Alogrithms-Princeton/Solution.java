import java.util.Arrays;
import java.util.PriorityQueue;

class Edge implements Comparable<Edge> {
    int v; // Index of one endpoint
    int w; // Index of the other endpoint
    int weight;

    Edge(int v, int w, int weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge that) {
        if (this.weight() < that.weight()) return -1;
        else if (this.weight() > that.weight()) return 1;
        return 0;
    }

    int weight() {
        return weight;
    }

    @Override
    public String toString() {
        return v + "\t" + w + "\t" + this.weight;
    }
}

class UF {
    private int[] parent;
    private int[] size;

    public UF(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int p) {
        while (p != parent[p]) {
            p = parent[p];
        }
        return p;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        } else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
    }
}

class Solution {

    public static void main(String[] args) {
        int[][] points = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
        int minCost = minCostConnectPoints(points);
        System.out.println("Minimum Cost: " + minCost);
    }

    public static int minCostConnectPoints(int[][] points) {
        // Make a PQ by weight
        PriorityQueue<Edge> edges = new PriorityQueue<>();

        for (int i = 0; i < points.length; i++) {
            int[] point1 = points[i];
            for (int j = i + 1; j < points.length; j++) {
                int[] point2 = points[j];
                int distance = Math.abs(point1[0] - point2[0]) + Math.abs(point1[1] - point2[1]);
                edges.add(new Edge(i, j, distance));
            }
        }

        int weight = 0;

        UF uf  = new UF(points.length);
        int count = 0 ;
        while(!edges.isEmpty() && count< points.length-1 ){
            Edge e = edges.poll();
            int v = e.v;
            int w = e.w;

            if(!uf.connected(v, w)){
                uf.union(v, w);
                count+=1;
                weight+= e.weight();
            }
        }
    
        return weight;
    }
}
