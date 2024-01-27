package com.example.shatranj;

import java.util.Comparator;

public abstract class Tile {

    private int tileCoordinate;

    Tile(int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }
    public int getTileCoordinate() {
        return tileCoordinate;
    }

    protected enum TILE_STATUS{
        OCCUPIED, EMPTY
    }

    public static Tile createTile(int tileCoordinate, Piece piece){
        if (piece != null){
            return new OccupiedTile(tileCoordinate,piece);
        } else {
            return new EmptyTile(tileCoordinate);
        }
    }
    public abstract TILE_STATUS getTILE_STATUS ();
    public abstract Piece getPiece();

}
final class EmptyTile extends Tile{
    EmptyTile(int TileCoordinate){
        super(TileCoordinate);
    }

    @Override
    public TILE_STATUS getTILE_STATUS(){
        return TILE_STATUS.EMPTY;
    }

    @Override
    public Piece getPiece(){
        return null;
    }

    @Override
    public String toString(){
        return "-";
    }
}

final class OccupiedTile extends Tile{
    private Piece tilePiece;

    OccupiedTile(int tileCoordinate, Piece tilePiece){
        super(tileCoordinate);
        this.tilePiece = tilePiece;
    }

    @Override
    public TILE_STATUS getTILE_STATUS (){
        return TILE_STATUS.OCCUPIED;
    }
    @Override
    public Piece getPiece (){
        return tilePiece;
    }

    @Override
    public String toString(){
        if (this.tilePiece.getPieceAlliance() == Alliance.BLACK){
            return getPiece().toString().toLowerCase();
        } else {
            return getPiece().toString();
        }
    }
}
class TileComparator implements Comparator<Tile>{

    @Override
    public int compare(Tile o1, Tile o2) {
        return o1.getTileCoordinate()- o2.getTileCoordinate();
    }
}
