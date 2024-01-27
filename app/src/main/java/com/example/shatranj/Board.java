package com.example.shatranj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private final ArrayList<Tile> gameBoard;
    private final ArrayList<Piece> whitePieces;
    private final ArrayList<Piece> blackPieces;
    private final BlackPlayer blackPlayer;
    private final WhitePlayer whitePlayer;
    private final Player currentPlayer;
    private static boolean isBoardEven = true;
    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

        final ArrayList<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final ArrayList<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.blackPlayer = new BlackPlayer(this, blackStandardLegalMoves, whiteStandardLegalMoves);
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        if (isBoardEven){
            currentPlayer = whitePlayer;
            isBoardEven = false;
        } else {
            currentPlayer = blackPlayer;
            isBoardEven = true;
        }
    }
    public Tile getTile(int tileCoordinate){
        return this.gameBoard.get(tileCoordinate);
    }
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }
    public Player getBlackPlayer(){
        return this.blackPlayer;
    }
    public Player getWhitePlayer(){
        return this.whitePlayer;
    }
    private ArrayList<Move> calculateLegalMoves(ArrayList<Piece> pieces){
        ArrayList<Move> moves = new ArrayList<>();
        for (Piece piece : pieces){
            moves.addAll(piece.calculateLegalMoves(this));
        }
        return moves;
    }
    private static ArrayList<Tile> createGameBoard(Builder builder){
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i< ChessUtils.NO_OF_TILES; i++){
            tiles.add(Tile.createTile(i, builder.boardConfig.get(i)));
        }
        tiles.sort(new TileComparator());
        return tiles;
    }
    public ArrayList<Piece> getWhitePieces(){
        return this.whitePieces;
    }
    public ArrayList<Piece> getBlackPieces(){
        return this.blackPieces;
    }
    private static ArrayList<Piece> calculateActivePieces(ArrayList<Tile> gameBoard, Alliance alliance){
        ArrayList<Piece> activePieces = new ArrayList<>();
        for (Tile tile : gameBoard){
            if (tile.getTILE_STATUS() == Tile.TILE_STATUS.OCCUPIED){
                Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return activePieces;
    }

    public ArrayList<Piece> getAllPieces(){
        ArrayList<Piece> blacks = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        ArrayList<Piece> white = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        ArrayList<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(blacks);
        allPieces.addAll(white);
        return allPieces;
    }
    public static Board standardBoard(){
        Builder builder = new Builder();

                //Black Side
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));

                //White side
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));

                //setting moves
        isBoardEven = true;

        return builder.build();
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i< ChessUtils.NO_OF_TILES; i++){
            String tileText = this.gameBoard.get(i).toString();
            builder.append(tileText);
            builder.append(' ');
            if ((i+1) % ChessUtils.NO_OF_COLUMNS == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }


    static class Builder{
        Map<Integer, Piece> boardConfig;
        Builder(){
            this.boardConfig = new HashMap<>();
        }
        public Builder setPiece(Piece piece){
            final Piece pieceToSet = setPieceInBuilder(piece);
            pieceToSet.setPiecePosition(piece.getPiecePosition());
            boardConfig.put(piece.getPiecePosition(), pieceToSet);
            return this;
        }
        public Builder setPiece(Piece movedPiece, int destinationCoordinate){
            final Piece pieceToSet = setPieceInBuilder(movedPiece);
            pieceToSet.setPiecePosition(destinationCoordinate);
            boardConfig.put(destinationCoordinate, pieceToSet);
            return this;
        }
        public Builder setPieceOnKill(Piece movedPiece, int killCoordinate){
            final Piece pieceToSet = setPieceInBuilder(movedPiece);
            pieceToSet.setPiecePosition(killCoordinate);
            boardConfig.put(killCoordinate,pieceToSet);
            boardConfig.put(movedPiece.getPiecePosition(),null);
            return this;
        }
        public Board build(){
            return new Board(this);
        }
        private Piece setPieceInBuilder(Piece piece) {
            if (piece.getPieceAlliance() == Alliance.WHITE) {
                if (piece.getPieceType() == Piece.PieceType.KNIGHT) {
                    return new Knight((Knight) piece);
                } else if (piece.getPieceType() == Piece.PieceType.BISHOP) {
                    return new Bishop((Bishop) piece);
                } else if (piece.getPieceType() == Piece.PieceType.KING) {
                    return new King((King) piece);
                } else if (piece.getPieceType() == Piece.PieceType.PAWN) {
                    return new Pawn((Pawn) piece);
                } else if (piece.getPieceType() == Piece.PieceType.ROOK) {
                    return new Rook((Rook) piece);
                } else if (piece.getPieceType() == Piece.PieceType.QUEEN) {
                    return new Queen((Queen) piece);
                }
            } else if (piece.getPieceAlliance() == Alliance.BLACK) {
                if (piece.getPieceType() == Piece.PieceType.KNIGHT) {
                    return new Knight((Knight) piece);
                } else if (piece.getPieceType() == Piece.PieceType.BISHOP) {
                    return new Bishop((Bishop) piece);
                } else if (piece.getPieceType() == Piece.PieceType.KING) {
                    return new King((King) piece);
                } else if (piece.getPieceType() == Piece.PieceType.PAWN) {
                    return new Pawn((Pawn) piece);
                } else if (piece.getPieceType() == Piece.PieceType.ROOK) {
                    return new Rook((Rook) piece);
                } else if (piece.getPieceType() == Piece.PieceType.QUEEN) {
                    return new Queen((Queen) piece);
                }
            }
            return null;
        }
    }
}