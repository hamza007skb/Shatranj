package com.example.shatranj;

import java.util.ArrayList;
import java.util.HashSet;

enum Alliance{
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }
        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    },
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }
        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    };

    public abstract int getDirection();

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
public abstract class Piece {
    private int piecePosition;
    private Alliance pieceAlliance;
    protected PieceType pieceType;
    protected final boolean isFirstMove;

    Piece(int piecePosition, Alliance pieceAlliance, PieceType pieceType){
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.pieceType = pieceType;
        this.isFirstMove = false;
    }
    Piece(Piece piece){
        this.copy(piece);
        this.isFirstMove = false;
    }
    @Override
    public boolean equals(Object object){
        if (!(object instanceof  Piece)){
            return false;
        }
        if(this == object){
            return true;
        }
        Piece piece = (Piece) object;
        return (this.getPiecePosition() == ((Piece) object).getPiecePosition() &&
                this.getPieceAlliance() == ((Piece) object).getPieceAlliance() &&
                this.getPieceType() == ((Piece) object).getPieceType() &&
                this.isFirstMove == piece.isFirstMove);
    }
    private Piece copy(Piece piece){
        this.piecePosition = piece.getPiecePosition();
        this.pieceType = piece.pieceType;
        this.pieceAlliance = piece.pieceAlliance;

        return this;
    }
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
    public void setPieceAlliance(Alliance pieceAlliance) {
        this.pieceAlliance = pieceAlliance;
    }
    public void setPiecePosition(int piecePosition) {
        this.piecePosition = piecePosition;
    }

    public void uniqueLegalMoves(ArrayList<Move> moves){
        HashSet<Move> uniqueMoves = new HashSet<>(moves);
        moves.clear();
        moves.addAll(uniqueMoves);
    }

    public int getPiecePosition() {
        return piecePosition;
    }
    public abstract ArrayList<Move> calculateLegalMoves (Board board);
    public abstract PieceType getPieceType();
    public abstract Piece movedPiece(Move move);

    enum PieceType{
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        };
        private String pieceName;
        PieceType(String pieceName){
            this.pieceName = pieceName;
        }
        @Override
        public String toString(){
            return this.pieceName;
        }
        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}

            ////KNIGHT CLASS
final class Knight extends Piece{
    final ArrayList<Move> legalMoves = new ArrayList<>();
    final int[] AVAILABLE_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17};
    Knight(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition,pieceAlliance, PieceType.KNIGHT);
    }
    Knight(Knight knight){
        super(knight);
    }
    @Override
    public ArrayList<Move> calculateLegalMoves(Board board){
        int destinationCoordiante;
        for (int i=0; i<AVAILABLE_MOVES.length; i++){
            destinationCoordiante = AVAILABLE_MOVES[i] + this.getPiecePosition();

            if (ChessUtils.isValidTileCoordinate(destinationCoordiante)){
                if (firstColumnExclusion(this.getPiecePosition(),AVAILABLE_MOVES[i]) ||
                        secondColumnExclusion(this.getPiecePosition(), AVAILABLE_MOVES[i]) ||
                        seventhColumnExclusion(this.getPiecePosition(), AVAILABLE_MOVES[i]) ||
                        eighthColumnExclusion(this.getPiecePosition(), AVAILABLE_MOVES[i]) ){
                    continue;
                }

                Tile pieceDestinationTile = board.getTile(destinationCoordiante);
                if (pieceDestinationTile.getTILE_STATUS() == Tile.TILE_STATUS.EMPTY){
                    legalMoves.add(new Move.NormalMove(this, board, destinationCoordiante));
                } else {
                    Piece pieceOnDestination = board.getTile(destinationCoordiante).getPiece();
                    Alliance pieceAlliance = pieceOnDestination.getPieceAlliance();

                    if (pieceAlliance != this.getPieceAlliance()){
                        legalMoves.add(new Move.AttackMove(this, board, destinationCoordiante, pieceOnDestination));
                    }
                }
            }
        }
        uniqueLegalMoves(legalMoves);
        return this.legalMoves;
    }

                @Override
    public PieceType getPieceType() {
        return pieceType;
    }

     @Override
    public Knight movedPiece(Move move) {
        return new Knight(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance());
    }

    private boolean firstColumnExclusion(int currentCoordinate, int moveToCoordinate){
        if ((currentCoordinate % ChessUtils.NO_OF_COLUMNS == ChessUtils.FIRST_COLOUM_INDEX) &&
                (moveToCoordinate == -17 || moveToCoordinate == -10 ||
                 moveToCoordinate == 6 || moveToCoordinate == 15)){
            return true;
        }
        return false;
    }
    private boolean secondColumnExclusion(int currentCoordinate, int moveToCoordinate){
        if ((currentCoordinate % ChessUtils.NO_OF_COLUMNS == ChessUtils.SECOND_COLUMN) &&
                ((moveToCoordinate == -10) || (moveToCoordinate == 6) )){
            return true;
        }
        return false;
    }
    private boolean seventhColumnExclusion(int currentCoordinate, int moveToCoordinate){
        if ((currentCoordinate % ChessUtils.NO_OF_COLUMNS == ChessUtils.SEVENTH_COLUMN) &&
                ((moveToCoordinate == -6) || (moveToCoordinate == 10))){
            return true;
        }
        return false;
    }
    private boolean eighthColumnExclusion(int currentCoordinate, int moveToCoordinate){
        if ((currentCoordinate % ChessUtils.NO_OF_COLUMNS == ChessUtils.EIGHT_COLOUM_INDEX) &&
                ((moveToCoordinate == -6) || (moveToCoordinate == 15)
                || (moveToCoordinate == 10) || (moveToCoordinate == 17))){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return (this.getPieceAlliance() == Alliance.BLACK) ? "nb" : "nw";
    }
}

            ////BISHOP CLASS
final class Bishop extends Piece{

    final ArrayList<Integer> leftDiagonalUpwardMoves = new ArrayList<>();
    final ArrayList<Integer> leftDiagonalDownwardMoves = new ArrayList<>();
    final ArrayList<Integer> rightDiagonalUpwardMoves = new ArrayList<>();
    final ArrayList<Integer> rightDiagonalDownwardMoves = new ArrayList<>();
    final ArrayList<Move> legalMoves = new ArrayList<>();

    Bishop(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.BISHOP);
    }
    Bishop(Bishop bishop){
        super(bishop);
    }
    private void setAVAILABLE_MOVES(int piecePosition){

        int leftDiagonalUpwards = piecePosition;
        int rightDiagonalUpwards = piecePosition;
        int leftDiagonalDownwards = piecePosition;
        int rightDiagonalDownwards = piecePosition;

        for (int i=0; i<8; i++) {
            if (!isInFirstColoumn(leftDiagonalUpwards) && ChessUtils.isValidTileCoordinate(leftDiagonalUpwards)) {
                leftDiagonalUpwards -= 9;
                if (ChessUtils.isValidTileCoordinate(leftDiagonalUpwards)) {
                    leftDiagonalUpwardMoves.add(leftDiagonalUpwards);
                }
            }
        }
        for (int i=0; i<8; i++) {
            if (!isInFirstColoumn(leftDiagonalDownwards) && ChessUtils.isValidTileCoordinate(leftDiagonalDownwards)) {
                leftDiagonalDownwards += 7;
                if (ChessUtils.isValidTileCoordinate(leftDiagonalDownwards)) {
                    leftDiagonalDownwardMoves.add(leftDiagonalDownwards);
                }
            }
        }
        for (int i=0; i<8; i++) {
            if (!isInEightColoumn(rightDiagonalUpwards) && ChessUtils.isValidTileCoordinate(rightDiagonalUpwards)) {
                rightDiagonalUpwards -= 7;
                if (ChessUtils.isValidTileCoordinate(rightDiagonalUpwards)) {
                    rightDiagonalUpwardMoves.add(rightDiagonalUpwards);
                }
            }
        }
        for (int i=0; i<8; i++) {
            if (!isInEightColoumn(rightDiagonalDownwards) && ChessUtils.isValidTileCoordinate(rightDiagonalDownwards)) {
                rightDiagonalDownwards += 9;
                if (ChessUtils.isValidTileCoordinate(rightDiagonalDownwards)) {
                    rightDiagonalDownwardMoves.add(rightDiagonalDownwards);
                }
            }
        }
    }

    private boolean isInEightColoumn(int position) {
        for (int i=0; i<ChessUtils.EIGHT_COLUMN.length; i++){
            if (position == ChessUtils.EIGHT_COLUMN[i]){
                return true;
            }
        }
        return false;
    }

    private boolean isInFirstColoumn(int position) {
        for (int i=0; i<ChessUtils.FIRST_COLUMN.length; i++){
            if (position == ChessUtils.FIRST_COLUMN[i]){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Move> getLegalMoves() {
        return legalMoves;
    }

    @Override
    public ArrayList<Move> calculateLegalMoves(Board board) {

        this.setAVAILABLE_MOVES(this.getPiecePosition());

        for (int i=0; i<leftDiagonalUpwardMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = leftDiagonalUpwardMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, leftDiagonalUpwardMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<leftDiagonalDownwardMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = leftDiagonalDownwardMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, leftDiagonalDownwardMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<rightDiagonalDownwardMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = rightDiagonalDownwardMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, rightDiagonalDownwardMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<rightDiagonalUpwardMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = rightDiagonalUpwardMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, rightDiagonalUpwardMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        uniqueLegalMoves(legalMoves);
        return this.legalMoves;
    }
    @Override
    public Bishop movedPiece(Move move) {
        return new Bishop(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance());
    }
    @Override
    public PieceType getPieceType() {
        return pieceType;
    }
    @Override
    public String toString(){
        return (this.getPieceAlliance() == Alliance.BLACK) ? "bb" : "bw";
    }
}

            ////QUEEN CLASS
final class Queen extends Piece{
    final ArrayList<Move> legalMoves = new ArrayList<>();
    final ArrayList<Integer> leftDiagonalUpwardMoves = new ArrayList<>();
    final ArrayList<Integer> leftDiagonalDownwardMoves = new ArrayList<>();
    final ArrayList<Integer> rightDiagonalUpwardMoves = new ArrayList<>();
    final ArrayList<Integer> rightDiagonalDownwardMoves = new ArrayList<>();
    final ArrayList<Integer> upVectorMoves = new ArrayList<>();
    final ArrayList<Integer> downVectorMoves = new ArrayList<>();
    final ArrayList<Integer> leftVectorMoves = new ArrayList<>();
    final ArrayList<Integer> rightVectorMoves = new ArrayList<>();

    Queen(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN);
    }
    Queen(Queen queen){
        super(queen);
    }
    private void setAVAILABLE_MOVES(int position) {

        int leftDiagonalUpwards = position;
        int rightDiagonalUpwards = position;
        int leftDiagonalDownwards = position;
        int rightDiagonalDownwards = position;
        int upVector = position;
        int downVector = position;
        int leftVector = position;
        int rightVector = position;

        for (int i=0; i<8; i++) {
            if (!isInFirstColoumn(leftDiagonalUpwards) && ChessUtils.isValidTileCoordinate(leftDiagonalUpwards)) {
                leftDiagonalUpwards -= 9;
                if (ChessUtils.isValidTileCoordinate(leftDiagonalUpwards)) {
                    leftDiagonalUpwardMoves.add(leftDiagonalUpwards);
                }
            }
        }
        for (int i=0; i<8; i++) {
            if (!isInFirstColoumn(leftDiagonalDownwards) && ChessUtils.isValidTileCoordinate(leftDiagonalDownwards)) {
                leftDiagonalDownwards += 7;
                if (ChessUtils.isValidTileCoordinate(leftDiagonalDownwards)) {
                    leftDiagonalDownwardMoves.add(leftDiagonalDownwards);
                }
            }
        }
        for (int i=0; i<8; i++) {
            if (!isInEightColoumn(rightDiagonalUpwards) && ChessUtils.isValidTileCoordinate(rightDiagonalUpwards)) {
                rightDiagonalUpwards -= 7;
                if (ChessUtils.isValidTileCoordinate(rightDiagonalUpwards)) {
                    rightDiagonalUpwardMoves.add(rightDiagonalUpwards);
                }
            }
        }
        for (int i=0; i<8; i++) {
            if (!isInEightColoumn(rightDiagonalDownwards) && ChessUtils.isValidTileCoordinate(rightDiagonalDownwards)) {
                rightDiagonalDownwards += 9;
                if (ChessUtils.isValidTileCoordinate(rightDiagonalDownwards)) {
                    rightDiagonalDownwardMoves.add(rightDiagonalDownwards);
                }
            }
        }
        for (int i=0; i<8; i++){
            if (!isInFirstColoumn(leftVector) && ChessUtils.isValidTileCoordinate(leftVector)){
                --leftVector;
                if (ChessUtils.isValidTileCoordinate(leftVector)){
                    leftVectorMoves.add(leftVector);
                }
            }
        }
        for (int i=0; i<8; i++){
            if (!isInEightColoumn(rightVector) && ChessUtils.isValidTileCoordinate(rightVector)){
                ++rightVector;
                if (ChessUtils.isValidTileCoordinate(rightVector)){
                    rightVectorMoves.add(rightVector);
                }
            }
        }
        for (int i=0; i<8; i++){
            if (ChessUtils.isValidTileCoordinate(upVector)){
                upVector -= 8;
                if (ChessUtils.isValidTileCoordinate(upVector)){
                    upVectorMoves.add(upVector);
                }
            }
        }
        for (int i=0; i<8; i++){
            if (ChessUtils.isValidTileCoordinate(downVector)){
                downVector += 8;
                if (ChessUtils.isValidTileCoordinate(downVector)){
                    downVectorMoves.add(downVector);
                }
            }
        }
    }

    private boolean isInEightColoumn(int position) {
        for (int i=0; i<ChessUtils.EIGHT_COLUMN.length; i++){
            if (position == ChessUtils.EIGHT_COLUMN[i]){
                return true;
            }
        }
        return false;
    }
    private boolean isInFirstColoumn(int position) {
        for (int i=0; i<ChessUtils.FIRST_COLUMN.length; i++){
            if (position == ChessUtils.FIRST_COLUMN[i]){
                return true;
            }
        }
        return false;
    }
    @Override
    public ArrayList<Move> calculateLegalMoves(Board board){
        this.setAVAILABLE_MOVES(this.getPiecePosition());

        for (int i=0; i<leftDiagonalUpwardMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = leftDiagonalUpwardMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, leftDiagonalUpwardMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<leftDiagonalDownwardMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = leftDiagonalDownwardMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, leftDiagonalDownwardMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<rightDiagonalUpwardMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = rightDiagonalUpwardMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, rightDiagonalUpwardMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<rightDiagonalDownwardMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = rightDiagonalDownwardMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, rightDiagonalDownwardMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<upVectorMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = upVectorMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, upVectorMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<downVectorMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = downVectorMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, downVectorMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<leftVectorMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = leftVectorMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, leftVectorMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<rightVectorMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = rightVectorMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, rightVectorMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        uniqueLegalMoves(legalMoves);
        return this.legalMoves;
    }
    @Override
    public Queen movedPiece(Move move) {
        return new Queen(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }
    @Override
    public String toString(){
        return (this.getPieceAlliance() == Alliance.BLACK) ? "qb" : "qw";
    }
}

                ////KING CLASS
final class King extends Piece{

    King(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.KING);
    }
    King(King king){
        super(king);
    }
    private final int[] AVAILABLE_MOVES = {7,8,9,1,-1,-7,-8,-9};
    private final ArrayList<Move> legalMoves = new ArrayList<>();

    @Override
    public ArrayList<Move> calculateLegalMoves(Board board) {

        for (int i=0; i<AVAILABLE_MOVES.length; i++){

            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate += AVAILABLE_MOVES[i];

            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)){
                if (isInFirstColoumn(this.getPiecePosition()) &&
                        (AVAILABLE_MOVES[i] == -1 || AVAILABLE_MOVES[i] == -9 || AVAILABLE_MOVES[i] == 7)){
                    continue;
                }
                if (isInEightColoumn(this.getPiecePosition()) &&
                        (AVAILABLE_MOVES[i] == 1 || AVAILABLE_MOVES[i] == -7 || AVAILABLE_MOVES[i] == 9)){
                    continue;
                }
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        legalMoves.add(new Move.AttackMove(this, board, destinationCoordinate, board.getTile(destinationCoordinate).getPiece()));
                    }
                } else {
                    legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        uniqueLegalMoves(legalMoves);
        return legalMoves;
    }
    private boolean isInEightColoumn(int position) {
        for (int i=0; i<ChessUtils.EIGHT_COLUMN.length; i++){
            if (position == ChessUtils.EIGHT_COLUMN[i]){
                return true;
            }
        }
        return false;
    }
    private boolean isInFirstColoumn(int position) {
        for (int i=0; i<ChessUtils.FIRST_COLUMN.length; i++){
            if (position == ChessUtils.FIRST_COLUMN[i]){
                return true;
            }
        }
        return false;
    }
    @Override
    public King movedPiece(Move move) {
        return new King(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public String toString(){
        return (this.getPieceAlliance() == Alliance.BLACK) ? "kb" : "kw";
    }
}

                ////PAWN CLASS
final class Pawn extends Piece{

    Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.PAWN);
    }
    Pawn(Pawn pawn){
        super(pawn);
    }
    private static final int[] WHITE_AVAILABLE_MOVES = {-8,-7,-9};
    private static final int[] BLACK_AVAILABLE_MOVES = {8,7,9};
    final ArrayList<Move> legalMoves = new ArrayList<>();
    @Override
    public ArrayList<Move> calculateLegalMoves(Board board) {

        if (this.getPieceAlliance() == Alliance.BLACK) {

            for (int i = 0; i < BLACK_AVAILABLE_MOVES.length; i++) {

                int destinationCoordinate = this.getPiecePosition() + BLACK_AVAILABLE_MOVES[i];
                if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {

                    if (isInFirstColoumn(this.getPiecePosition()) &&
                            (BLACK_AVAILABLE_MOVES[i] == 7)){
                        continue;
                    }
                    if (isInEightColoumn(this.getPiecePosition()) &&
                            (BLACK_AVAILABLE_MOVES[i] == 9)){
                        continue;
                    }
                    if (BLACK_AVAILABLE_MOVES[i] == 7 || BLACK_AVAILABLE_MOVES[i] == 9) {
                        if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                            if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                                legalMoves.add(new Move.AttackMove(this, board, destinationCoordinate, board.getTile(destinationCoordinate).getPiece()));
                            }
                        }
                    }
                    if (BLACK_AVAILABLE_MOVES[i] == 8 && ChessUtils.isValidTileCoordinate(destinationCoordinate)){
                        legalMoves.add(new Move.NormalMove(this, board, destinationCoordinate));
                    }
                }
            }
            uniqueLegalMoves(legalMoves);
            return this.legalMoves;
        } else {
            for (int i = 0; i < WHITE_AVAILABLE_MOVES.length; i++) {

                int destinationCoordinate = this.getPiecePosition() + WHITE_AVAILABLE_MOVES[i];
                if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {

                    if (isInFirstColoumn(this.getPiecePosition()) &&
                            (WHITE_AVAILABLE_MOVES[i] == -9)){
                        continue;
                    }
                    if (isInEightColoumn(this.getPiecePosition()) &&
                            (WHITE_AVAILABLE_MOVES[i] == -7)){
                        continue;
                    }
                    if (WHITE_AVAILABLE_MOVES[i] == -7 || WHITE_AVAILABLE_MOVES[i] == -9) {
                        if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                            if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                                legalMoves.add(new Move.AttackMove(this, board, destinationCoordinate, board.getTile(destinationCoordinate).getPiece()));
                            }
                        }
                    }
                    if (WHITE_AVAILABLE_MOVES[i] == -8 && ChessUtils.isValidTileCoordinate(destinationCoordinate)){
                        legalMoves.add(new Move.NormalMove(this, board, destinationCoordinate));
                    }
                }
            }
            uniqueLegalMoves(legalMoves);
            return this.legalMoves;
        }
    }
    private boolean isInEightColoumn(int position) {
        for (int i=0; i<ChessUtils.EIGHT_COLUMN.length; i++){
            if (position == ChessUtils.EIGHT_COLUMN[i]){
                return true;
            }
        }
        return false;
    }
    private boolean isInFirstColoumn(int position) {
        for (int i=0; i<ChessUtils.FIRST_COLUMN.length; i++) {
            if (position == ChessUtils.FIRST_COLUMN[i]) {
                return true;
            }
        }
        return false;
    }
    @Override
    public Pawn movedPiece(Move move) {
        return new Pawn(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance());
    }
    @Override
    public PieceType getPieceType() {
        return pieceType;
    }
    @Override
    public String toString(){
        return (this.getPieceAlliance() == Alliance.BLACK) ? "pb" : "pw";
    }
}

                ////ROOK CLASS
final class Rook extends Piece{
    private final ArrayList<Integer> AVAILABLE_MOVES = new ArrayList<>();
    private final ArrayList<Move> legalMoves = new ArrayList<>();
    final ArrayList<Integer> upVectorMoves = new ArrayList<>();
    final ArrayList<Integer> downVectorMoves = new ArrayList<>();
    final ArrayList<Integer> leftVectorMoves = new ArrayList<>();
    final ArrayList<Integer> rightVectorMoves = new ArrayList<>();

    Rook(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.ROOK);
    }
    Rook(Rook rook){
        super(rook);
    }
    private void setAVAILABLE_MOVES(int position) {
        int upVector = position;
        int downVector = position;
        int leftVector = position;
        int rightVector = position;

        for (int i=0; i<8; i++){
            if (!isInFirstColoumn(leftVector) && ChessUtils.isValidTileCoordinate(leftVector)){
                --leftVector;
                if (ChessUtils.isValidTileCoordinate(leftVector)){
                    leftVectorMoves.add(leftVector);
                }
            }
        }
        for (int i=0; i<8; i++){
            if (!isInEightColoumn(rightVector) && ChessUtils.isValidTileCoordinate(rightVector)){
                ++rightVector;
                if (ChessUtils.isValidTileCoordinate(rightVector)){
                    rightVectorMoves.add(rightVector);
                }
            }
        }
        for (int i=0; i<8; i++){
            if (ChessUtils.isValidTileCoordinate(upVector)){
                upVector -= 8;
                if (ChessUtils.isValidTileCoordinate(upVector)){
                    upVectorMoves.add(upVector);
                }
            }
        }
        for (int i=0; i<8; i++){
            if (ChessUtils.isValidTileCoordinate(downVector)){
                downVector += 8;
                if (ChessUtils.isValidTileCoordinate(downVector)){
                    downVectorMoves.add(downVector);
                }
            }
        }
    }
    private boolean isInEightColoumn(int position) {
        for (int i=0; i<ChessUtils.EIGHT_COLUMN.length; i++){
            if (position == ChessUtils.EIGHT_COLUMN[i]){
                return true;
            }
        }
        return false;
    }
    private boolean isInFirstColoumn(int position) {
        for (int i=0; i<ChessUtils.FIRST_COLUMN.length; i++) {
            if (position == ChessUtils.FIRST_COLUMN[i]) {
                return true;
            }
        }
        return false;
    }
    @Override
    public ArrayList<Move> calculateLegalMoves(Board board) {

        this.setAVAILABLE_MOVES(this.getPiecePosition());

        for (int i=0; i<upVectorMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = upVectorMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, upVectorMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<downVectorMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = downVectorMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, downVectorMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<leftVectorMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = leftVectorMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, leftVectorMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        for (int i=0; i<rightVectorMoves.size(); i++) {
            int destinationCoordinate = this.getPiecePosition();
            destinationCoordinate = rightVectorMoves.get(i);
            if (ChessUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (board.getTile(destinationCoordinate).getTILE_STATUS() != Tile.TILE_STATUS.EMPTY) {
                    if (board.getTile(destinationCoordinate).getPiece().getPieceAlliance() != this.getPieceAlliance()) {
                        this.legalMoves.add(new Move.AttackMove(this, board, rightVectorMoves.get(i), board.getTile(destinationCoordinate).getPiece()));
                        break;
                    } else if(board.getTile(destinationCoordinate).getPiece().getPieceAlliance() == this.getPieceAlliance()){
                        break;
                    }
                } else {
                    this.legalMoves.add(new Move.NormalMove(this,board,destinationCoordinate));
                }
            }
        }
        uniqueLegalMoves(legalMoves);
        return this.legalMoves;
    }
    @Override
    public Rook movedPiece(Move move) {
        return new Rook(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance());
    }
    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public String toString(){
        return (this.getPieceAlliance() == Alliance.BLACK) ? "rb" : "rw";
    }
}