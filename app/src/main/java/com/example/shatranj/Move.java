package com.example.shatranj;

import java.util.HashSet;
import java.util.Set;

public abstract class Move {
    protected final Piece movedPiece;
    protected final Board board;
    private final int destinationCoordinates;
    private int getCurrentCoordinate() {
        return getMovedPiece().getPiecePosition();
    }
    protected Move(Piece movedPiece, Board board, int destinationCoordinates) {
        this.movedPiece = movedPiece;
        this.board = board;
        this.destinationCoordinates = destinationCoordinates;
    }

    @Override
    public String toString(){
        return String.format(String.valueOf(this.destinationCoordinates));
    }
    @Override
    public int hashCode(){
        int result = 1;
        result = 31 * result + destinationCoordinates;
        result = 31 * result + movedPiece.hashCode();
        return result;
    }
    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (!(o instanceof Move)){
            return false;
        }
        Move move = (Move) o;
        return getDestinationCoordinates() == move.getDestinationCoordinates() &&
                getMovedPiece().equals(move.getMovedPiece());
    }

    public int getDestinationCoordinates(){
        return this.destinationCoordinates;
    }


    public Piece getMovedPiece() {
        return this.movedPiece;
    }
    public abstract Board execute();

    public boolean isAttackMove() {
        return false;
    }
    public Piece getAttackedPiece() {
        return null;
    }
    static class AttackMove extends Move{
        private Piece attackedPiece;

        public AttackMove(Piece movedPiece, Board board, int destinationCoordinates, Piece attackedPiece) {
            super(movedPiece, board, destinationCoordinates);
            this.attackedPiece = attackedPiece;
        }
        @Override
        public boolean isAttackMove(){
            return true;
        }
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AttackMove)){
                return false;
            }
            AttackMove attackMove = (AttackMove) o;
            return getAttackedPiece().equals(attackMove.getAttackedPiece()) &&
                    super.equals(attackMove);
        }
        @Override
        public int hashCode(){
            return this.getAttackedPiece().hashCode() + super.hashCode();
        }
        @Override
        public Board execute(){
            Board.Builder builder = new Board.Builder();
            for (Piece piece: this.board.getAllPieces()){
                if (!piece.movedPiece(this).equals(piece)){
                    builder.setPiece(piece);
                }
            }
            builder.setPieceOnKill(movedPiece, this.getDestinationCoordinates());
            return builder.build();
        }
    }

    static class NormalMove extends Move {
        public NormalMove(Piece movedPiece, Board board, int destinationCoordinates) {
            super(movedPiece, board, destinationCoordinates);
        }
        @Override
        public Board execute(){
            Board.Builder builder = new Board.Builder();
            for (Piece piece: this.board.getAllPieces()){
                if (!piece.equals(movedPiece)){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(movedPiece, this.getDestinationCoordinates());
            return builder.build();
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }
    }
}
