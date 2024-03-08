import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private Digraph graph;
    private BreadthFirstDirectedPaths bfssource;
    private BreadthFirstDirectedPaths bfsdest;
   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G){
    this.graph = G;
   }

   private boolean isValid(int v){
    return v>-1 && v < graph.V();
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
    if (!isValid(v) || !isValid(w)) throw new IllegalArgumentException();

    int ancestorVal = ancestor(v, w);
    if (ancestorVal==-1) return -1;
    return bfssource.distTo(ancestorVal) + bfsdest.distTo(ancestorVal);
}

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w) {
    if (!isValid(v) || !isValid(w)) throw new IllegalArgumentException("Invalid vertice");
    bfssource = new BreadthFirstDirectedPaths(this.graph, v);
    bfsdest = new BreadthFirstDirectedPaths(this.graph, w);

    int ancestorNode = -1;
    int minimumLength = Integer.MAX_VALUE;
    int currentLength = 0;

    for (int currentVertex=0; currentVertex<this.graph.V(); currentVertex++) {
        if (bfssource.hasPathTo(currentVertex) && bfsdest.hasPathTo(currentVertex)) {
            currentLength = bfssource.distTo(currentVertex) + bfsdest.distTo(currentVertex);
            if (currentLength < minimumLength) {
                ancestorNode = currentVertex;
                minimumLength = currentLength;
            }
        }
    }
    return ancestorNode;
}

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
    if (v==null || w==null) throw new IllegalArgumentException();

    int ancestorVal = ancestor(v, w);
    if (ancestorVal==-1) return -1;
    return bfssource.distTo(ancestorVal) + bfsdest.distTo(ancestorVal);
}

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
    if (v==null || w==null) throw new IllegalArgumentException();

    for (int i: v) {
        if (i<0 && i>=this.graph.V())
        throw new IllegalArgumentException();
    }

    bfssource = new BreadthFirstDirectedPaths(graph, v);
    bfsdest = new BreadthFirstDirectedPaths(graph, w);

    int ancestorNode = -1;
    int minimumLength = Integer.MAX_VALUE;
    int currentLength = 0;

    for (int currentVertex=0; currentVertex<this.graph.V(); currentVertex++) {
        if (bfssource.hasPathTo(currentVertex) && bfsdest.hasPathTo(currentVertex)) {
            currentLength = bfssource.distTo(currentVertex) + bfsdest.distTo(currentVertex);
            if (currentLength < minimumLength) {
                ancestorNode = currentVertex;
                minimumLength = currentLength;
            }
        }
    }
    return ancestorNode;
   
}

   // do unit testing of this class
   public static void main(String[] args){}
}