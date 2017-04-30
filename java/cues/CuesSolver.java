package cues;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

// 30 Minute Time Limit
// Solver for Cues Game
// By Third State Studio
public class CuesSolver {

    private static class Position {
        int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Position(Position cpy) {
            this.x = cpy.x;
            this.y = cpy.y;
        }

        void hit(Position ball) {
            ball.x += (ball.x - this.x);
            ball.y += (ball.y - this.y);
        }

        boolean at(Position target) {
            return target.x == this.x && target.y == this.y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    private final Position win;
    private final Position ball;
    private final Position[] cues;

    private CuesSolver(Position ball, Position win, Position... cues) {
        this.win = win;
        this.ball = ball;
        this.cues = cues;
    }

    private List<Position> solve() {
        Queue<List<Position>> moves = new ArrayDeque<>();
        moves.add(new ArrayList<>());
        while (!moves.isEmpty()) {
            List<Position> curr = moves.remove();
            if (check(curr))
                return curr;
            for (Position cue : cues) {
                List<Position> cpy = new ArrayList<>(curr);
                cpy.add(cue);
                moves.add(cpy);
            }
        }
        return null;
    }

    private boolean check(List<Position> moves) {
        Position cpy = new Position(ball);
        for (Position move : moves)
            move.hit(cpy);
        return cpy.at(win);
    }

    public static void main(String[] args) {
        // Level 13
        System.out.println(new CuesSolver(
                new Position(3, 2),
                new Position(1, 4),
                new Position(0, 5),
                new Position(5, 1),
                new Position(6, 3),
                new Position(6, 5),
                new Position(1, 0)).solve());
    }
}
