import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

public class Board {

    // Board is immutable
    private final int n;
    private final int[][] tiles;
    private final int hamming;
    private final int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];
        int tempH = 0;
        int tempM = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                this.tiles[i][j] = tile;
                if (tile == 0) {
                    continue;
                }

                int goalX = (tile - 1) / n;
                int goalY = tile - goalX * n - 1;

                if (i != goalX || j != goalY) {
                    tempH++;
                    tempM += Math.abs(i - goalX) + Math.abs(j - goalY);
                }
            }
        }

        hamming = tempH;
        manhattan = tempM;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || this.getClass() != y.getClass()) {
            return false;
        }

        Board b = (Board) y;
        if (this.n != b.n) {
            return false;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != b.tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();

        int zeroX = 0;
        int zeroY = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    zeroX = i;
                    zeroY = j;
                    break;
                }
            }
        }

        // 4 possible directions
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : dirs) {
            int nextX = zeroX + dir[0];
            int nextY = zeroY + dir[1];
            if (nextX < 0 || nextY < 0 || nextX >= n || nextY >= n) {
                continue;
            }
            int[][] neighbor = copyTiles();
            neighbor[zeroX][zeroY] = neighbor[nextX][nextY];
            neighbor[nextX][nextY] = 0;
            neighbors.add(new Board(neighbor));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = copyTiles();
        // 2 <= N < 128
        if (tiles[0][0] != 0 && tiles[0][1] != 0) {
            twinTiles[0][0] = tiles[0][1];
            twinTiles[0][1] = tiles[0][0];
        } else {
            twinTiles[1][0] = tiles[1][1];
            twinTiles[1][1] = tiles[1][0];
        }

        return new Board(twinTiles);
    }

    // return a copy of a board
    private int[][] copyTiles() {
        int[][] copy = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        System.out.println(initial);
        System.out.println("hamming: " + initial.hamming());
        System.out.println("manhattan: " + initial.manhattan());
        System.out.println("twin: " + initial.twin());
        Iterable<Board> neighbors = initial.neighbors();
        for (Board neighbor : neighbors) {
            System.out.println(neighbor);
        }
    }
}
