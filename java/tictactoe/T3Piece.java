package tictactoe;

public enum T3Piece {

    X, O;

    public T3Piece next() {
        return (this == X) ? O : X;
    }

}
