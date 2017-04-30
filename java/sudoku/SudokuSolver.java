package sudoku;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "unused"})
public class SudokuSolver {

    // Board Information
    private final int[] board;
    private final int boardRange;
    private final int cellRange;
    private final int subRange;

    // Temp & Helper Fields
    private final boolean[] used;
    private final int[][] validationIndices;

    // Implementation
    public SudokuSolver(int[] board) {

        // Initialize Board Information
        this.board = board;
        this.boardRange = board.length;
        this.cellRange = (int) Math.sqrt(boardRange);
        this.subRange = (int) Math.sqrt(cellRange);

        // Validate Board State
        if ((cellRange * cellRange != boardRange) || (subRange * subRange != cellRange))
            throw new RuntimeException("4th root of board length must be an integer.");

        for (int cell : board)
            if (cell < 0 || cell > cellRange)
                throw new RuntimeException("Board can not have cell value " + cell);

        // Temp Field For Validation
        this.used = new boolean[cellRange];

        // Validation Constraint Indices
        this.validationIndices = new int[cellRange * 3][cellRange];
        for (int i = 0, ii = cellRange, iii = cellRange + cellRange; i < cellRange; i++, ii++, iii++) {
            for (int j = 0; j < cellRange; j++) {
                validationIndices[i][j] = (i * cellRange) + j;
                validationIndices[ii][j] = i + (j * cellRange);
                validationIndices[iii][j] = ((i % subRange * subRange) + (i / subRange * cellRange * subRange)) + ((j % subRange) + (j / subRange * cellRange));
            }
        }
    }

    public void showSolution(boolean time) {
        long start = 0, end = 0, delta = 0;

        if (time) start = System.nanoTime();
        int[] solution = solve();
        if (time) end = System.nanoTime();

        StringBuilder sb = new StringBuilder();

        if (time) {
            delta = end - start;
            sb.append(String.format(
                    "Solution Time : %d ns / %d ms / %d s\n",
                    delta,
                    TimeUnit.MILLISECONDS.convert(delta, TimeUnit.NANOSECONDS),
                    TimeUnit.SECONDS.convert(delta, TimeUnit.NANOSECONDS)
            ));
        }

        for (int i = 0; i < cellRange; i++) {

            for (int j = 0; j < cellRange; j++) {
                sb.append((j != 0 && j % subRange == 0) ? "  |  " : "   ");
                sb.append(solution[i * cellRange + j]);
            }

            sb.append("\n");

            if ((i != cellRange - 1) && (i % subRange == subRange - 1)) {
                sb.append("   ");
                for (int k = 0; k <= (4 * cellRange); k++) {
                    sb.append("-");
                }
                sb.append("\n");
            }
        }

        System.out.println(sb.toString());
    }

    public int[] solve() {
        return solve(this.board);
    }

    private int[] solve(int[] board) {
        if (isValid(board)) {
            int nextIndex = -1;
            for (int i = 0; i < boardRange; i++) {
                if (board[i] == 0) {
                    nextIndex = i;
                    break;
                }
            }

            if (nextIndex == -1) return board;

            int[] nextBoard = Arrays.copyOf(board, boardRange);
            for (int i = 1; i <= cellRange; i++) {
                nextBoard[nextIndex] = i;
                int[] solution = solve(nextBoard);
                if (solution != null) {
                    return solution;
                }
            }
        }

        return null;
    }

    private boolean isValid(int[] board) {
        for (int[] boardIndices : validationIndices)
            if (!isValid(board, boardIndices))
                return false;
        return true;
    }

    private boolean isValid(int[] board, int[] boardIndices) {

        for (int i = 0; i < cellRange; i++)
            used[i] = false;

        for (int boardIndex : boardIndices) {
            int cell = board[boardIndex];
            if (cell != 0) {
                if (used[cell - 1]) {
                    return false;
                } else {
                    used[cell - 1] = true;
                }
            }
        }

        return true;
    }

    // Testing
    public static void main(String[] args) throws IOException {
        new SudokuSolver(new int[]{
                0, 5, 0, 2, 0, 0, 4, 0, 9,
                0, 3, 0, 0, 6, 0, 5, 0, 0,
                0, 0, 0, 0, 0, 3, 0, 0, 7,
                0, 0, 3, 0, 0, 0, 0, 0, 4,
                5, 0, 2, 4, 8, 6, 3, 0, 1,
                8, 0, 0, 0, 0, 0, 6, 0, 0,
                1, 0, 0, 8, 0, 0, 0, 0, 0,
                0, 0, 5, 0, 4, 0, 0, 2, 0,
                7, 0, 9, 0, 0, 5, 0, 4, 0
        }).showSolution(true);
    }

}
