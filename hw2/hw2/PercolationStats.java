package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private double[] ans;

    private int len;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        ans = new double[T];
        len = T;
        for (int i = 0; i < T; i++) {
            int count = 0;
            Percolation temp = pf.make(N);
            while (!temp.percolates()) {
                int temp1 = StdRandom.uniform(0, N);
                int temp2 = StdRandom.uniform(0, N);
                if (temp.isOpen(temp1, temp2)) {
                    continue;
                }
                temp.open(temp1, temp2);
                count++;
            }
            ans[i] = (double) count / (double)(N * N);
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(ans);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(ans);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return this.mean() - 1.96 * (this.stddev() / Math.sqrt(len));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return this.mean() + 1.96 * (this.stddev() / Math.sqrt(len));
    }


}
