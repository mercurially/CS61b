package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF unionFind;
    private WeightedQuickUnionUF unionFindNoBottom;
    private int countOpenSite;
    private int edgePointerCount;
    private int VIRTUAL_TOP_SITE_INDEX;
    private int VIRTUAL_BOTTOM_SITE_INDEX;

    //create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Argument N to constructor() is wrong");
        }

        edgePointerCount = N;
        VIRTUAL_TOP_SITE_INDEX = N * N;
        VIRTUAL_BOTTOM_SITE_INDEX = N * N + 1;
        grid = new boolean[N][N];
        unionFind = new WeightedQuickUnionUF(N * N + 2);
        unionFindNoBottom = new WeightedQuickUnionUF(N * N + 1);
        countOpenSite = 0;
    }

    //help to transform [row][col] to [index]
    private int gridToInt(int row, int col) {
        isValid(row, col);
        return row * edgePointerCount + col;
    }

    //open the site (row, col) if it is not open already
    public void open(int row, int col) {
        isValid(row, col);
        if (isOpen(row, col)) {
            return;
        }

        grid[row][col] = true;
        countOpenSite++;

        //union the opened vicinity
        int index = gridToInt(row, col);
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            int index1 = gridToInt(row - 1, col);
            unionFind.union(index, index1);
            unionFindNoBottom.union(index, index1);
        }
        if (row + 1 < edgePointerCount && isOpen(row + 1, col)) {
            int index2 = gridToInt(row + 1, col);
            unionFind.union(index, index2);
            unionFindNoBottom.union(index, index2);
        }
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            int index3 = gridToInt(row, col - 1);
            unionFind.union(index, index3);
            unionFindNoBottom.union(index, index3);
        }
        if (col + 1 < edgePointerCount && isOpen(row, col + 1)) {
            int index4 = gridToInt(row, col + 1);
            unionFind.union(index, index4);
            unionFindNoBottom.union(index, index4);
        }

        /*
         * If the site is in the top row, connect to the virtual top site.
         * Else if the site in the bottom row, connect it to the bottom virtual site.
         */
        if (row == 0) {
            unionFind.union(VIRTUAL_TOP_SITE_INDEX, gridToInt(row, col));
            unionFindNoBottom.union(VIRTUAL_TOP_SITE_INDEX, gridToInt(row, col));
        }

        if (col == edgePointerCount - 1) {
            unionFind.union(VIRTUAL_BOTTOM_SITE_INDEX, gridToInt(row, col));
        }

    }

    //is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        isValid(row, col);
        return grid[row][col];
    }

    private void isValid(int row, int col) {
        int n = edgePointerCount, m = edgePointerCount;
        if (row < 0 || row >= n || col < 0 || col >= m) {
            throw new IndexOutOfBoundsException();
        }
    }

    //is the site (row, col) full? --> ?
    public boolean isFull(int row, int col) {
        isValid(row, col);

        if (!grid[row][col]) {
            return false;
        } else {
            int index = gridToInt(row, col);
            return unionFind.connected(VIRTUAL_TOP_SITE_INDEX, index)
                    && unionFindNoBottom.connected(VIRTUAL_TOP_SITE_INDEX, index);
        }
    }

    //number of opne site
    public int numberOfOpenSites() {
        return countOpenSite;
    }

    //does the system percolate?
    public boolean percolate() {
        return unionFind.connected(VIRTUAL_TOP_SITE_INDEX, VIRTUAL_BOTTOM_SITE_INDEX);
    }

    // use for unit testing (not required, but keep this here for the autograder)
    public static void main(String[] args) {

    }


}
