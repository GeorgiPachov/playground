package chessGame;

public class Move {
    public Square getSource() {
        return source;
    }

    public void setSource(Square source) {
        this.source = source;
    }

    public Square getDestination() {
        return destination;
    }

    public void setDestination(Square destination) {
        this.destination = destination;
    }

    public Move(Square source, Square destination) {

        this.source = source;
        this.destination = destination;
    }

    private Square source;
    private Square destination;
}
