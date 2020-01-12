import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;


public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode previous;
        private final int moves;
        private final int priority;

        SearchNode(Board board, SearchNode previous) {
            this.board  = board;
            this.previous = previous;
            if (previous != null)
                this.moves = previous.moves + 1;
            else
                this.moves = 0;
            this.priority = this.board.manhattan() + this.moves;
        }
        Board previousBoard() {
            if (previous == null)
                return null;
            else
                return previous.board;
        }

        @Override
        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }
    private SearchNode solutionNode;
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        MinPQ<SearchNode> originQueue = new MinPQ<>();
        originQueue.insert(new SearchNode(initial, null));
        MinPQ<SearchNode> twinQueue = new MinPQ<>();
        twinQueue.insert(new SearchNode(initial.twin(), null));

        boolean solutionFound = false;
        while (!solutionFound) {
            SearchNode originNode = originQueue.delMin();
            SearchNode twinNode = twinQueue.delMin();

            if (originNode.board.isGoal()) {
                solutionNode = originNode;
                solutionFound = true;
            }
            else if (twinNode.board.isGoal()) {
                solutionFound = true;
            }
            else {
                for (Board board : originNode.board.neighbors()) {
                    if (!board.equals(originNode.previousBoard())) {
                        originQueue.insert(new SearchNode(board, originNode));
                    }
                }
                for (Board board : twinNode.board.neighbors()) {
                    if (!board.equals(twinNode.previousBoard())) {
                        twinQueue.insert(new SearchNode(board, twinNode));
                    }
                }
            }
        }
    }

    public boolean isSolvable() {
        return solutionNode != null;
    }

    public int moves() {
        if (isSolvable())
            return solutionNode.moves;
        else
            return -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        Stack<Board> stack = new Stack<>();
        SearchNode node = solutionNode;
        while (node != null) {
            stack.push(node.board);
            node = node.previous;
        }
        return stack;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = StdIn.readInt();
        Board initial = new Board(blocks);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            System.out.println("No solutions!");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
        }
    }
}
