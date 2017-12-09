package chessGame;

public class Move {
    public int oldX, oldY;
    public int newX, newY;

    public Move(int oldX, int oldY, int newX, int newY) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    public int getOldX() {
        return oldX;
    }

    public void setOldX(int oldX) {
        this.oldX = oldX;
    }

    @Override
    public String toString() {
        return "Move{" +
                "oldX=" + oldX +
                ", oldY=" + oldY +
                ", newX=" + newX +
                ", newY=" + newY +
                '}';
    }

    public int getOldY() {
        return oldY;
    }

    public void setOldY(int oldY) {
        this.oldY = oldY;
    }

    public int getNewX() {
        return newX;
    }

    public void setNewX(int newX) {
        this.newX = newX;
    }

    public int getNewY() {
        return newY;
    }

    public void setNewY(int newY) {
        this.newY = newY;
    }


}
