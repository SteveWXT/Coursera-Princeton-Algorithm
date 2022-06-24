/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private boolean[][] openSizes;
    private int openNum = 0;
    private final int topRoot;
    private final int bottomRoot;
    private final int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    // uf with two pseudo site on the top and bottom
    private final WeightedQuickUnionUF uf;
    // uf with one pseudo site on the top, for checking whether a size is fulls
    private final WeightedQuickUnionUF ufFull;



    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
        openSizes = new boolean[n][n];
        topRoot = 0;
        bottomRoot = n * n + 1;
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufFull = new WeightedQuickUnionUF(n * n + 1);

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                openSizes[i][j] = false;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException();
        }

        if (!isOpen(row, col)) {
            openSizes[row-1][col-1] = true;
            openNum++;

            // top line: connect to topRoot
            if (row == 1) {
                uf.union(xyTo1D(row, col), 0);
                ufFull.union(xyTo1D(row, col), 0);
            }
            // bottom line: connect to bottomRoot
            if (row == n) {
                uf.union(xyTo1D(row, col), n * n + 1);
            }

            for (int[] dir : dirs) {
                int tempRow = row + dir[0];
                int tempCol = col + dir[1];
                if (validate(tempRow, tempCol) && isOpen(tempRow, tempCol)) {
                    uf.union(xyTo1D(row, col), xyTo1D(tempRow, tempCol));
                    ufFull.union(xyTo1D(row, col), xyTo1D(tempRow, tempCol));
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException();
        }
        return openSizes[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException();
        }
        return ufFull.find(xyTo1D(row, col)) == ufFull.find(topRoot);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openNum;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(topRoot) == uf.find(bottomRoot);
    }

    // Helper Methods

    // calculate the 1D coordinate, starting from 1
    private int xyTo1D(int x, int y) {
        return (x-1) * n + y;
    }

    // check whether a coordinates is valid
    private boolean validate(int x, int y) {
        return !(x < 1 || x > n || y < 1 || y > n);
    }

    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        perc.open(1, 1);
        perc.open(2, 1);
        perc.open(3, 1);
        System.out.println(perc.percolates());
    }
}
