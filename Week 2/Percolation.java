import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF unionMatrix;
    private final WeightedQuickUnionUF unionMatrixForFull;
    private final int size, top, bottom;
    private int opened;
    private final boolean[][] cellsMatrix;
    /**
     * Створюємо матрицю N-на-N, з усіма заблокованими об'єктами
     * @param n - розмір матриці
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        // N*N матриця + 2 комірки - top та bottom
        unionMatrix = new WeightedQuickUnionUF(n*n + 2);
        unionMatrixForFull = new WeightedQuickUnionUF(n * n + 2);

        cellsMatrix = new boolean[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j)
                cellsMatrix[i][j] = false;
        }

        size = n;
        top = n*n;
        bottom = n*n+1;
        opened = 0;
    }
    /**
     * Відкрити комірку з індексом (i, j)
     * @param i - рядок
     * @param j - стовпчик
     */
    public void open(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size)
            throw new IllegalArgumentException();
        if (isOpen(i, j))
            return;

        int index = convertTo1DIndex(i -1, j - 1);
        cellsMatrix[i-1][j-1] = true;

        if (i+1 <= size && isOpen(i+1, j)) {
            unionMatrix.union(index, (i) * size + j - 1);
            unionMatrixForFull.union(index, (i) * size + j - 1);
        }
        if (i-1 > 0 && isOpen(i-1, j)) {
            unionMatrix.union(index, (i - 2) * size + j - 1);
            unionMatrixForFull.union(index, (i - 2) * size + j - 1);
        }
        if (j-1 > 0 && isOpen(i, j-1)) {
            unionMatrix.union(index, (i - 1) * size + j - 2);
            unionMatrixForFull.union(index, (i - 1) * size + j - 2);
        }
        if (j+1 <= size && isOpen(i, j+1)) {
            unionMatrix.union(index, (i - 1) * size + j);
            unionMatrixForFull.union(index, (i - 1) * size + j);
        }
        if (i == 1) {
            unionMatrix.union(index, top);
            unionMatrixForFull.union(index, top);
        }
        if (i == size)
            unionMatrix.union(index, bottom);
        opened++;
    }
    public boolean isOpen(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size)
            throw new IllegalArgumentException();
        return cellsMatrix[i-1][j-1];
    }
    public boolean isFull(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size)
            throw new IllegalArgumentException();
        int index = convertTo1DIndex(i - 1, j - 1);
        return (unionMatrixForFull.connected(index, top) && isOpen(i, j));
    }

    private int convertTo1DIndex(int i, int j) {
        return i * size + j;
    }

    /**
     * Рахуємо і повертаємо кількість відкритих комірок
     * @return кількість відкритих комірок
     */
    public int numberOfOpenSites() {
        return opened;
    }
    /**
     * Перевірити чи протікає
     * @return
     */
    public boolean percolates() {
        return unionMatrix.connected(top, bottom);
    }
}
