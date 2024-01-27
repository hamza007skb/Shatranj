package com.example.shatranj;

public class ChessUtils {

    static final int NO_OF_COLUMNS = 8;
    static final int NO_OF_ROWS = 8;
    static final int[] FIRST_COLUMN = {0,8,16,24,32,40,48,56};
    static final int[] EIGHT_COLUMN = {7,15,23,31,39,47,55,63};
    static final int FIRST_COLOUM_INDEX = 0;
    static final int EIGHT_COLOUM_INDEX = 7;
    static final int SECOND_COLUMN = 1;
    static final int SEVENTH_COLUMN = 6;
    static final int BOTTOM_RIGHT_CORNER = 63;
    static final int BOTTOM_LEFT_CORNER = 55;
    static final int TOP_RIGHT_CORNER = 7;
    static final int TOP_LEFT_CORNER = 0;
    static final int[] SECOND_ROW = {8,9,10,11,12,13,14,15};
    static final int[] SEVENTH_ROW = {48,49,50,51,52,53,53,55};
    static final int NO_OF_TILES = 64;
    static final int FIRST_TILE = 0;
    static final int WHITE_ROOK_TILE_RIGHT = 63;
    static final int WHITE_ROOK_TILE_LEFT = 56;
    static final int BLACK_ROOK_TILE_LEFT = 0;
    static final int BLACK_ROOK_TILE_RIGHT = 7;

    public static boolean isValidTileCoordinate (int coordinate){
        return coordinate >= 0 && coordinate <= 63;
    }
}
