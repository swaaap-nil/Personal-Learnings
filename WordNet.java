import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

public class WordNet {
    private int count;
    private HashMap<String, ArrayList<Integer>> synset;
    private HashMap<Integer, String> nounMap;
    private Digraph hypernym;
    private SAP sap;
    private DirectedCycle cycle;

    public WordNet(String synsets, String hypernyms) {
        this.count = 0;
        In insyn = new In(synsets);
        synset = new HashMap<>();
        nounMap = new HashMap<>();
        while (insyn.hasNextLine()) {
            String[] syn = insyn.readLine().split(",");
            String[] nounsArray = syn[1].split(" ");
            int synid = Integer.parseInt(syn[0]);

            nounMap.put(synid, syn[1]);

            for (int i=0; i<nounsArray.length; i++) {
                if (synset.containsKey(nounsArray[i])) {
                    synset.get(nounsArray[i]).add(synid);
                } else {
                    synset.put(nounsArray[i], new ArrayList<Integer>(Collections.singletonList(synid)));
                }
            }
            count++;
        }


        In inhyp = new In(hypernyms);
        hypernym = new Digraph(count);
        while (inhyp.hasNextLine()) {
            String[] relation = inhyp.readLine().split(",");
            Integer hypo = Integer.parseInt(relation[0]);
            for (int i = 1; i < relation.length; i++) {
                hypernym.addEdge(hypo, Integer.parseInt(relation[i]));
            }

        }

        this.sap = new SAP(hypernym);

        cycle = new DirectedCycle(hypernym);
        if (cycle.hasCycle()) throw new IllegalArgumentException("The given graph forms a directed cyclic graph.");

    }

    public Iterable<String> nouns() {
        Iterator<String> key = synset.keySet().iterator();
        ArrayList<String> noun = new ArrayList<>();
        while (key.hasNext()) {
            noun.add(key.next());
        }
        return noun;
    }

    public boolean isNoun(String word) {
        return synset.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            return this.sap.length(synset.get(nounA), synset.get(nounB));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String sap(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            int ancestorVal = this.sap.ancestor(synset.get(nounA), synset.get(nounB));
            return nounMap.get(ancestorVal);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
    }
}