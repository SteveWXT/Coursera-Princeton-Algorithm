import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Deque;
import java.util.LinkedList;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Solver {

    private final SearchNode solution;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode pre;
        private final int priority;
        private final int manhattanCache;

        public SearchNode(Board board, SearchNode pre) {
            this.board = board;
            this.pre = pre;
            this.moves = pre == null ? 0 : pre.moves + 1;
            this.manhattanCache = board.manhattan();
            this.priority = moves + manhattanCache;
        }

        public void insertNeighbors(MinPQ<SearchNode> pq) {
            for (Board neighbor : this.board.neighbors()) {
                if (pre != null && pre.board.equals(neighbor)) {
                    continue;
                }

                pq.insert(new SearchNode(neighbor, this));
            }
        }

        public int compareTo(SearchNode that) {
            if (this.priority == that.priority) {
                return this.manhattanCache - that.manhattanCache;
            }
            return this.priority - that.priority;
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<SearchNode> rawPQ = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();

        rawPQ.insert(new SearchNode(initial, null));
        twinPQ.insert(new SearchNode(initial.twin(), null));

        while (true) {
            SearchNode rawNode = rawPQ.delMin();
            SearchNode twinNode = twinPQ.delMin();

            if (rawNode.board.isGoal()) {
                solution = rawNode;
                break;
            }

            if (twinNode.board.isGoal()) {
                solution = null;
                break;
            }

            rawNode.insertNeighbors(rawPQ);
            twinNode.insertNeighbors(twinPQ);
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        } else {
            return solution.moves;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }

        Deque<Board> seq  = new LinkedList<>();
        SearchNode cur = solution;
        while (cur != null) {
            seq.addFirst(cur.board);
            cur = cur.pre;
        }

        return seq;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
