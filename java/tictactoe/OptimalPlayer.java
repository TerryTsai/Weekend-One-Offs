package tictactoe;

import java.util.Arrays;

public class OptimalPlayer implements T3Player {

    private static class Node {
        // Implementation
        final T3Piece player;
        final T3Piece[] board;
        final Node[] children;

        Node(T3Piece player, T3Piece[] board, Node[] children) {
            this.player = player;
            this.board = board;
            this.children = children;
        }

        int getScore(T3Piece forPlayer) {

            if (T3Engine.didWin(board, forPlayer))
                return 1;
            if (T3Engine.didWin(board, forPlayer.next()))
                return -1;
            if (T3Engine.isFull(board) || children == null || children.length == 0)
                return 0;

            if (forPlayer == player) {
                int max = Integer.MIN_VALUE;
                for (Node child : children) {
                    int childScore = child.getScore(forPlayer);
                    max = (childScore > max) ? childScore : max;
                }
                return max;

            } else {
                int min = Integer.MAX_VALUE;
                for (Node child : children) {
                    int childScore = child.getScore(forPlayer);
                    min = (childScore < min) ? childScore : min;
                }
                return min;
            }
        }

        // Static Utility
        static Node buildTree(T3Piece[] board, T3Piece player) {

            int idx = 0;
            Node[] children = new Node[9];

            for (int i = 0; i < board.length; i++) {
                if (board[i] == null) {
                    T3Piece[] childBoard = Arrays.copyOf(board, 9);
                    childBoard[i] = player;
                    children[idx++] = buildTree(childBoard, player.next());
                }
            }

            children = Arrays.copyOf(children, idx);
            return new Node(player, board, children);
        }
    }

    @Override
    public int getMove(T3Piece[] board, T3Piece piece) {

        Node root = Node.buildTree(board, piece);
        Node bestNode = null;
        int bestScore = Integer.MIN_VALUE;

        for (Node child : root.children) {
            int childScore = child.getScore(piece);
            if (bestScore < childScore) {
                bestScore = childScore;
                bestNode = child;
            }
        }

        if (bestNode == null)
            throw new RuntimeException("Unexpected null node");

        for (int i = 0; i < board.length; i++)
            if (board[i] != bestNode.board[i])
                return i;

        throw new RuntimeException("Unexpected end of method");
    }

}
