package tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPlayer implements T3Player {

    private final List<Integer> freeList;
    private final Random random;

    public RandomPlayer() {
        this.freeList = new ArrayList<>(9);
        this.random = new Random();
    }

    @Override
    public int getMove(T3Piece[] board, T3Piece piece) {
        freeList.clear();
        for (int i = 0; i < board.length; i++)
            if (board[i] == null)
                freeList.add(i);

        return freeList.get(random.nextInt(freeList.size()));
    }

}
