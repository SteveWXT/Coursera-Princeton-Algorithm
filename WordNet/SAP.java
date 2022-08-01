import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class SAP {

    private final Digraph dig;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.dig = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(dig, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(dig, w);

        int minPath = Integer.MAX_VALUE;
        for (int x = 0; x < dig.V(); x++) {
            if (bfs1.hasPathTo(x) && bfs2.hasPathTo(x)) {
                minPath = Math.min(minPath, bfs1.distTo(x) + bfs2.distTo(x));
            }
        }

        return minPath == Integer.MAX_VALUE ? -1 : minPath;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(dig, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(dig, w);

        int minPath = Integer.MAX_VALUE;
        int ans = -1;
        for (int x = 0; x < dig.V(); x++) {
            if (bfs1.hasPathTo(x) && bfs2.hasPathTo(x)) {
                int curPath = bfs1.distTo(x) + bfs2.distTo(x);
                if (curPath < minPath) {
                    minPath = curPath;
                    ans = x;
                }
            }
        }

        return ans;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        if (!hasValue(v) || !hasValue(w)) {
            return -1;
        }

        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(dig, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(dig, w);

        int minPath = Integer.MAX_VALUE;
        for (int x = 0; x < dig.V(); x++) {
            if (bfs1.hasPathTo(x) && bfs2.hasPathTo(x)) {
                minPath = Math.min(minPath, bfs1.distTo(x) + bfs2.distTo(x));
            }
        }

        return minPath == Integer.MAX_VALUE ? -1 : minPath;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        if (!hasValue(v) || !hasValue(w)) {
            return -1;
        }

        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(dig, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(dig, w);

        int minPath = Integer.MAX_VALUE;
        int ans = -1;
        for (int x = 0; x < dig.V(); x++) {
            if (bfs1.hasPathTo(x) && bfs2.hasPathTo(x)) {
                int curPath = bfs1.distTo(x) + bfs2.distTo(x);
                if (curPath < minPath) {
                    minPath = curPath;
                    ans = x;
                }
            }
        }

        return ans;
    }

    private boolean hasValue(Iterable<Integer> v) {
        Iterator<Integer> it = v.iterator();
        return it.hasNext();
    }

    // do unit testing of this class
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
