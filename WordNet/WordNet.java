/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordNet {

    private Digraph wordNet;
    //  a noun can appear in more than one synset
    private Map<String, Set<Integer>> nounToSynsetId;
    private List<String> idToSynsets;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        nounToSynsetId = new HashMap<>();
        idToSynsets = new ArrayList<>();
        readSynsets(synsets);
        wordNet = new Digraph(idToSynsets.size());
        readHypernyms(hypernyms);
        checkCycle();
        checkRoot();
        sap = new SAP(wordNet);
    }

    // read file contains synsets
    private void readSynsets(String synsets) {
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String[] fields = in.readLine().split(",");
            idToSynsets.add(fields[1]);
            String[] nounces = fields[1].split(" ");
            for (String nounce: nounces) {
                if (!nounToSynsetId.containsKey(nounce)) {
                    Set<Integer> ids = new HashSet<>();
                    ids.add(Integer.parseInt(fields[0]));
                    nounToSynsetId.put(nounce, ids);
                } else {
                    nounToSynsetId.get(nounce).add(Integer.parseInt(fields[0]));
                }
            }
        }
    }

    // read file contains hypernyms
    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] fields = in.readLine().split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                wordNet.addEdge(v, w);
            }
        }
    }

    // check is there a cycle in the WordNet
    private void checkCycle() {
        DirectedCycle cycle = new DirectedCycle(wordNet);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException();
        }
    }

    // check is there more than one root in the WordNet
    private void checkRoot() {
        int root = 0;
        for (int v = 0; v < wordNet.V(); v++) {
            if (wordNet.outdegree(v) == 0) {
                root++;;
                if (root > 1) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToSynsetId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounToSynsetId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return sap.length(nounToSynsetId.get(nounA), nounToSynsetId.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return idToSynsets.get(sap.ancestor(nounToSynsetId.get(nounA), nounToSynsetId.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
