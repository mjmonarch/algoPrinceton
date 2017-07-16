/******************************************************************************
 *  Program:      PercolationStats.java
 *  Author:       Kamenskyi Sergii
 *  Date:         jun 2017
 *
 *  Dependencies: edu.princeton.cs.algs4.StdRandom,
 *                edu.princeton.cs.algs4.StdRandom;
 *                edu.princeton.cs.algs4.StdOut
 *
 *  Purpose:      assignment #1 from algs course week 1
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    //private variables
    private int trials;                //quantity of computations
    private double[] res;              //probability when percolates
    private boolean one_trial = false; //special case for trials = 1

    //perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0) throw new IllegalArgumentException("invalid value for dimension");
        if (trials <= 0) throw new IllegalArgumentException("invalid value for quantity of trials");

        Percolation per;
        int row, col;
        this.trials = trials;
        res = new double[trials];

        if (trials == 1) one_trial = true;
        for (int i = 0; i < trials; i++) {
            per = new Percolation(n);
            while (!per.percolates()) {
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;
                per.open(row, col);
            }
            res[i] = (double) per.numberOfOpenSites() /(n * n);
        }
    }

    //sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(res);
    }

    //sample standard deviation of percolation threshold
    public double stddev() {
        if (one_trial) return Double.NaN; //dealing with special case
        return StdStats.stddev(res);
    }

    //low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    //high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    //test client
    public static void main(String[] args) {
        StdOut.println("starting test client");
        StdOut.println("1. test on grid 200x200 with 100 trials");
        PercolationStats a = new PercolationStats(200, 100);
        StdOut.println("mean =" + a.mean());
        StdOut.println("stddev =" + a.stddev());
        StdOut.println("95% confidence interval = (" + a.confidenceLo() + ", " + a.confidenceHi() + ")");
        StdOut.println("1. test on grid 2x2 with 10000 trials");
        a = new PercolationStats(2, 10000);
        StdOut.println("mean =" + a.mean());
        StdOut.println("stddev =" + a.stddev());
        StdOut.println("95% confidence interval = (" + a.confidenceLo() + ", " + a.confidenceHi() + ")");
    }
}
