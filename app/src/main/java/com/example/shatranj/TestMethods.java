package com.example.shatranj;

import java.util.Scanner;

public class TestMethods {

    static final Scanner sc = new Scanner(System.in);
    static final Board board = Board.standardBoard();
    public static void main(String[] args) {

        System.out.println(board.toString());

        for (int i=0; i<board.getCurrentPlayer().getActivePieces().size(); i++){
            System.out.print(board.getCurrentPlayer().getActivePieces().get(i).toString() + (i)+ " ");
        }
        System.out.println();

        System.out.println(board.getCurrentPlayer().getActivePieces().get(0).calculateLegalMoves(board).toString());
        Move move = board.getCurrentPlayer().getActivePieces().get(0).calculateLegalMoves(board).get(0);

        Board newBoard = move.execute();
        System.out.println(newBoard.toString());


//        while (board.getBlackPlayer().kingValidation() && board.getWhitePlayer().kingValidation()){
//            tellPlayer(board);
//            System.out.println("select Piece:");
//            selectPiece(board);
//            int selectPiece = sc.nextInt();
//            getMoves(board, selectPiece);
//        }

    }
//    static String tellPlayer(Board board){
//        return board.getCurrentPlayer().toString();
//    }
//    static int selectPiece(Board board){
//        ArrayList<Piece> pieces = new ArrayList<>();
//        pieces.addAll(board.getCurrentPlayer().getActivePieces());
//
//        for (int i=0; i<pieces.size(); i++){
//            System.out.print(pieces.get(i).toString() + (i) + " ");
//        }
//        System.out.println();
//        System.out.println("select Piece: ");
//        int selectPiece = sc.nextInt();
//
//        return selectPiece;
//    }
//    static Board getMoves(Board board, int piece){
//        ArrayList<Move> moves = new ArrayList<>();
//        moves.addAll(board.getCurrentPlayer().getActivePieces().get(piece).calculateLegalMoves(board));
//        for (int i=0; i<moves.size(); i++){
//            System.out.print(moves.get(i) + " ");
//        }
//        System.out.println();
//
//        System.out.println("select move: ");
//        int selectMove = sc.nextInt();
//
//        return moves.get(selectMove).execute();
//    }
}
