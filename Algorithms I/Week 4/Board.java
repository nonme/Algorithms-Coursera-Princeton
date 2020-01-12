import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdIn;

import java.util.Arrays;

public class Board {
    private final int n;
    private int[][] tiles;

    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; ++i)
            this.tiles[i] = tiles[i].clone();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n);
        stringBuilder.append("\n"); // System.lineSeparator();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                stringBuilder.append(tiles[i][j]);
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int distance = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0)
                    continue;
                if (tiles[i][j] != (n * i + j + 1))
                    distance++;
            }
        }
        return distance;
    }

    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0)
                    continue;
                int row = (tiles[i][j] - 1) / n;
                int col = (tiles[i][j] - 1) % n;
                int rowsToTile = Math.abs(i - row);
                int colsToTile = Math.abs(j - col);
                distance += (rowsToTile + colsToTile);
            }
        }
        return distance;
    }

    public boolean isGoal() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] != (i * n + j + 1) % (n * n)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (n == that.n) {
            for (int i = 0; i < n; ++i) {
                if (Arrays.compare(this.tiles[i], that.tiles[i]) != 0)
                    return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public Iterable<Board> neighbors() {
        Bag<Board> boards = new Bag<>();
        int row = 0;
        int col = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        if (row != 0)
            boards.add(getNeighbor(row, col, -1, 0));
        if (row != n - 1)
            boards.add(getNeighbor(row, col, 1, 0));
        if (col != 0)
            boards.add(getNeighbor(row, col, 0, -1));
        if (col != n - 1)
            boards.add(getNeighbor(row, col, 0, 1));
        return boards;
    }

    private Board getNeighbor(int row, int col, int verticalStep, int horizontalStep) {
        Board board = new Board(tiles);
        swapTiles(board, row, col, row + verticalStep, col + horizontalStep);
        return board;
    }

    private void swapTiles(Board board, int row1, int col1, int row2, int col2) {
        int temp = board.tiles[row1][col1];
        board.tiles[row1][col1] = board.tiles[row2][col2];
        board.tiles[row2][col2] = temp;
    }

    public Board twin() {
        Board twinBoard = new Board(tiles);
        for (int i = 0; i < n; ++i) {
            if (twinBoard.tiles[0][i] != 0 && twinBoard.tiles[1][i] != 0) {
                swapTiles(twinBoard, 0, i, 1, i);
                break;
            }
        }
        return twinBoard;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = StdIn.readInt();
        Board initial = new Board(blocks);


        System.out.println(initial.manhattan());
        System.out.println(initial.hamming());
    }
}
