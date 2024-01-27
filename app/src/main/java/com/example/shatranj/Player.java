package com.example.shatranj;

import java.util.ArrayList;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final ArrayList<Move> legalMoves;
    private boolean isInCheck;

    Player(Board board, ArrayList<Move> legalMoves, ArrayList<Move> opponentMoves){
        this.board = board;
        this.legalMoves = legalMoves;
        this.playerKing = isKingPresent();
        this.isInCheck = !Player.calculateAttackOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    protected static ArrayList<Move.AttackMove> calculateAttackOnTile(int piecePosition, ArrayList<Move> opponentMoves) {
        ArrayList<Move.AttackMove> attackMoves = new ArrayList<>();
        for (Move move : opponentMoves){
            if(move.getDestinationCoordinates() == piecePosition){
                attackMoves.add((Move.AttackMove) move);
            }
        }
        return attackMoves;
    }
    public ArrayList<Move> getLegalMoves(){
        return this.legalMoves;
    }
    private King isKingPresent(){
        for (Piece piece : getActivePieces()){
            if (piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Invalid Board!!! Should not be reached");
    }
    public boolean kingValidation(){
        for (Piece piece : getActivePieces()){
            if (piece.getPieceType().isKing()){
                return true;
            }
        }
        throw new RuntimeException("Invalid Board!!! Should not be reached");
    }

    public String moveInfo(Move move){
        return this.makeMove(move).toString();
    }
    public abstract ArrayList<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponentPlayer();
    public boolean isMoveLegal(Move move){
        return this.legalMoves.contains(move);
    }
    private boolean hasEscapeMove() {
        for (Move move : this.legalMoves){
            MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }
    public MoveTransition makeMove(Move move){
        if (!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        Board transitionedBoard = move.execute();
        ArrayList<Move.AttackMove> kingAttacks = Player.calculateAttackOnTile(transitionedBoard.getCurrentPlayer().playerKing.getPiecePosition(),
                                                                    transitionedBoard.getCurrentPlayer().getLegalMoves());
        if (!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.PLAYER_ON_CHECK);
        }
        return new MoveTransition(transitionedBoard, move, MoveStatus.DONE);
    }
}

class BlackPlayer extends Player {

    BlackPlayer(Board board, ArrayList<Move> blackStandardLegalMoves, ArrayList<Move> whiteStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }
    @Override
    public ArrayList<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponentPlayer() {
        return this.board.getWhitePlayer();
    }

}
class WhitePlayer extends Player{

    WhitePlayer(Board board, ArrayList<Move> whiteStandardLegalMoves, ArrayList<Move> blackStandardLegalMoves){
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public ArrayList<Piece> getActivePieces(){
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponentPlayer() {
        return this.board.getBlackPlayer();
    }

}
