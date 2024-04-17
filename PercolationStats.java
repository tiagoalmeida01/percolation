import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final double meanValue;
    private final double stddevValue;

    // perform independent trials on an n-by-n grid
    /*
     * Calcular a média e a stddev imediatamente após calcular o p* ajuda a melhorar
     * o timming e memória do programa
     */
    public PercolationStats(int n, int trials) {
        validate(n, trials);
        this.trials = trials;
        final double[] threshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1; // +1 bc cannot be 0
                int col = StdRandom.uniform(n) + 1;
                p.open(row, col);
            }
            threshold[i] = (double) p.numberOfOpenSites() / (n * n); // p*

        }
        meanValue = StdStats.mean(threshold);
        stddevValue = StdStats.stddev(threshold);

    }

    // sample mean of percolation threshold
    public double mean() {
        return meanValue;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddevValue;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return meanValue - CONFIDENCE_95 * stddevValue / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return meanValue + CONFIDENCE_95 * stddevValue / Math.sqrt(trials);
    }

    private void validate(int n, int t) {
        if (n <= 0)
            throw new IllegalArgumentException("The grid size must be larger than zero");

        if (t <= 0)
            throw new IllegalArgumentException("The number of experiments must be larger than zero");

    }

    // test client (see below)
    public static void main(String[] args) {
        /*
         * Integer.parseInt converte os valores recebidos pelo vetor de argumentos da
         * main em argumenos do tipo int necessários para correr o programa.
         */
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean                                 = " + ps.meanValue);
        System.out.println("stddev                               = " + ps.stddevValue);
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");

    }
}
