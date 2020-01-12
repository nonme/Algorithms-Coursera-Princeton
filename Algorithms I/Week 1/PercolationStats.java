import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int experiments;
    private final double[] results;
    private final double coeff = 1.96;
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    /**
     * Проведемо T експериментів по відкриттю матриці N*N
     * @param n - розмір матриці
     * @param t - кількість експериментів
     */
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0)
            throw new IllegalArgumentException();
        experiments = t;
        results = new double[t];
        for (int c = 0; c < t; ++c) {
            Percolation result = new Percolation(n);
            while (!result.percolates()) {
                int i = StdRandom.uniform(0, n) + 1;
                int j = StdRandom.uniform(0, n) + 1;
                while (result.isOpen(i, j)) {
                    i = StdRandom.uniform(0, n) + 1;
                    j = StdRandom.uniform(0, n) + 1;
                }
                result.open(i, j);
            }
            double p = result.numberOfOpenSites() / ((double) (n * n));
            results[c] = p;
        }

        mean         = StdStats.mean(results);
        stddev       = StdStats.stddev(results);
        confidenceLo = mean - coeff * stddev / Math.sqrt(experiments);
        confidenceHi = mean + coeff * stddev / Math.sqrt(experiments);
    }

    /**
     * Порахуємо середнє
     * @return середнє
     */
    public double mean() {
        return mean;
    }

    /**
     * Порахуємо похибку
     * @return похибка
     */
    public double stddev() {
        return stddev;
    }
    /*
    Нижня грань інтервалу довіри
     */
    public double confidenceLo() {
        return confidenceLo;
    }
    /*
    Верхня грань інтервалу довіри
     */
    public double confidenceHi() {
        return confidenceHi;
    }
    public String toString() {
        return
                "mean                    = " + mean() + "\n" +
                "stddev                  = " + stddev() + "\n" +
                "95% confidence interval = [" + confidenceLo() + ", " + confidenceHi() + "]";
    }
    public static void main(String[] args) {
        int n = 0, t = 0;
        try {
            n = Integer.parseUnsignedInt(args[0]);
            t = Integer.parseUnsignedInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("You need to input two non-negative numbers.");
        }
        System.out.println(new PercolationStats(n, t));
    }
}
