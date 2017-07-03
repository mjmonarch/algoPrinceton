/******************************************************************************
 *  Program:      Percolation.java
 *  Author:       Kamenskyi Sergii
 *  Date:         jun 2017
 *
 *  Dependencies: edu.princeton.cs.algs4.StdIn,
 *                edu.princeton.cs.algs4.WeightedQuickUnionUF,
 *
 *  Purpose:      assignment #1 from algs course week 1
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    //private variables
    private boolean [] grid_status;    //true - open, false - closed
    private WeightedQuickUnionUF grid;
    private int dim;                   // dimension
    private int open_q;                // quantity of open sites
    private int last;                  // address of last site

    //transform (x,y) to 1D attribute
    private int xyto1D (int x, int y) {
        return (x-1)*dim + y;
    }

    //validate array indexes
    private void validate_indexes (int row, int column) {
        if (row < 0 || row > dim)
            throw new IndexOutOfBoundsException("row index " + row + " is not between 0 and " + dim);
        if (column < 0 || column > dim)
            throw new IndexOutOfBoundsException("column index " + column + " is not between 0 and " + dim);
    }

    //constructor: create n-by-n grid, with all sites blocked
    public Percolation (int n) {
        if (n <= 0) throw new IllegalArgumentException("invalid value for dimension");
        dim = n;
        open_q = 0;
        last = n*n + 1;
        grid_status = new boolean[last+1];
        grid = new WeightedQuickUnionUF(last+1);
    }

    //open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate_indexes(row, col);
        int r = xyto1D(row,col);
        //open site
        if (grid_status[r])
            return;
        grid_status[r] = true;
        open_q++;

        //trying to connect to upper neighbour, if it is open
        if (row == 1)
            grid.union(0, r);
        else if (grid_status[r-dim])
            grid.union(r,r-dim);
        //trying to connect to bottom neighbour, if it is open
        if (row == dim)
            grid.union(last, r);
        else if (grid_status[r+dim])
            grid.union(r,r+dim);
        //trying to connect to left neighbour, if it exist and is open
        if ((col != 1) && (grid_status[r-1]))
            grid.union(r-1, r);
        //trying to connect to right neighbour, if it exist and is open
        if ((col != dim) && (grid_status[r+1]))
            grid.union(r, r+1);
    }

    //is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate_indexes(row,col);
        return grid_status[xyto1D(row,col)];
    }

    //is site (row, col) full?
    public boolean isFull(int row, int col)  {
        validate_indexes(row,col);
        return grid.connected(xyto1D(row,col),0);
    }

    //number of open sites
    public int numberOfOpenSites() {
        return open_q;
    }

    //does the system percolate?
    public boolean percolates() {
        return grid.connected(0, last);
    }

    //test client
    public static void main(String[] args) {
        StdOut.println("starting test client");
        StdOut.print("1. test on 2x2 grid; ");
        Percolation a = new Percolation(2);
        StdOut.println("system percolates? " + a.percolates());
        a.open(1,1);
        StdOut.println("open site (1,1) -  system percolates? " + a.percolates());
        a.open(2,2);
        StdOut.println("open site (2,2) -  system percolates? " + a.percolates());
        a.open(2,1);a.open(1,1);
        StdOut.println("open site (2,1) -  system percolates? " + a.percolates());
        StdOut.println("quantity of open sites is " + a.numberOfOpenSites());
        StdOut.println("__________testing isOpen and isFull________________");
        StdOut.println("call isOpen on (1,2) - " + a.isOpen(1,2));
        StdOut.println("call isFull on (1,2) - " + a.isFull(1,2));
        StdOut.println("call isOpen on (2,2) - " + a.isOpen(2,2));
        StdOut.println("call isFull on (2,2) - " + a.isFull(2,2));
        StdOut.print("2. test on 3x3 grid; ");
        a = new Percolation(3);
        StdOut.println("system percolates? " + a.percolates());
        a.open(1,1);
        StdOut.println("open site (1,1) -  system percolates? " + a.percolates());
        a.open(2,1);
        StdOut.println("open site (2,1) -  system percolates? " + a.percolates());
        a.open(3,1);a.open(1,1);
        StdOut.println("open site (3,1) -  system percolates? " + a.percolates());
        StdOut.println("quantity of open sites is " + a.numberOfOpenSites());
        StdOut.print("2. test on 1x1 grid; ");
        a = new Percolation(1);
        StdOut.println("system percolates? " + a.percolates());
        a.open(1,1);
        StdOut.println("open site (1,1) -  system percolates? " + a.percolates());
        StdOut.println("quantity of open sites is " + a.numberOfOpenSites());
    }
}
