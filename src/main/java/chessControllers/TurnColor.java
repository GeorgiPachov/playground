package chessControllers;
public enum TurnColor {
    white, black;

    public TurnColor opposite(){
        if(this == white)
            return black;
        else
            return white;
    }
}