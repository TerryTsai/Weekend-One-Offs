package tictactoe;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SuppressWarnings("UnusedAssignment")
public class ConsolePlayer implements T3Player {

    private final BufferedReader reader;

    public ConsolePlayer() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public int getMove(T3Piece[] board, T3Piece piece) {
        int input = -1;
        do {
            System.out.print("?");
            try {
                input = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                input = -1;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        } while (input < 0 || input > board.length);
        return input;
    }
}
