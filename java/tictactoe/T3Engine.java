package tictactoe;

@SuppressWarnings("UnusedAssignment")
public final class T3Engine {

    private final T3Piece[] board;
    private final T3Player xPlayer;
    private final T3Player oPlayer;

    public T3Engine(T3Player xPlayer, T3Player oPlayer) {
        this.board = new T3Piece[9];
        this.xPlayer = xPlayer;
        this.oPlayer = oPlayer;
    }

    public void play(T3Piece start) {
        for (int i = 0; i < board.length; i++) board[i] = null;

        T3Piece currPiece = start;
        T3Player currPlayer = (start == T3Piece.O) ? oPlayer : xPlayer;
        while (!isFull(board) && !didWin(board, T3Piece.O) && !didWin(board, T3Piece.X)) {
            printBoard(board);
            int move = -1;

            do {
                move = currPlayer.getMove(board, currPiece);
            } while (move < 0 || move >= board.length || board[move] != null);

            System.out.println(currPiece.name() + " @ " + move + "\n-----");
            board[move] = currPiece;
            currPiece = currPiece.next();
            currPlayer = (currPlayer == oPlayer) ? xPlayer : oPlayer;
        }

        printBoard(board);

        if (didWin(board, T3Piece.O))
            System.out.println("O Wins!");
        else if (didWin(board, T3Piece.X))
            System.out.println("X Wins!");
        else
            System.out.println("Tie Game!");

    }

    static void printBoard(T3Piece[] board) {
        for (int i = 0; i < board.length; i++) {
            if (i != 0 && i % 3 == 0)
                System.out.println();

            if (board[i] == null)
                System.out.print(i + " ");
            else
                System.out.print(board[i].name() + " ");
        }
        System.out.println("\n");
    }

    public static boolean didWin(T3Piece[] board, T3Piece piece) {
        return (piece != null) && (
                (board[0] == piece && board[1] == piece && board[2] == piece) ||
                (board[3] == piece && board[4] == piece && board[5] == piece) ||
                (board[6] == piece && board[7] == piece && board[8] == piece) ||
                (board[0] == piece && board[3] == piece && board[6] == piece) ||
                (board[1] == piece && board[4] == piece && board[7] == piece) ||
                (board[2] == piece && board[5] == piece && board[8] == piece) ||
                (board[0] == piece && board[4] == piece && board[8] == piece) ||
                (board[2] == piece && board[4] == piece && board[6] == piece));
    }

    public static boolean isFull(T3Piece[] board) {
        return (board[0] != null) && (board[1] != null) && (board[2] != null) &&
                (board[3] != null) && (board[4] != null) && (board[5] != null) &&
                (board[6] != null) && (board[7] != null) && (board[8] != null);
    }

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++)
            new T3Engine(new OptimalPlayer(), new OptimalPlayer()).play(T3Piece.O);

    }

}
