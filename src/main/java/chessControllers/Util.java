package chessControllers;

public class Util {
    public static void logV(String s) {
        if (Constants.DEBUG_MOVE_SORTING) {
            System.out.println(s);
        }
    }
}
