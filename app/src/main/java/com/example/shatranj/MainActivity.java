package com.example.shatranj;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
//////SP23-BAI-018 HAMZA AHMAD
//////SP23-BAI-20 HUSSAIN RAZA
public class MainActivity extends AppCompatActivity {

    private final ArrayList<Board> BOARDS = new ArrayList<>();
    private final ArrayList<Piece> MOVED_PIECES = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Board board = Board.standardBoard();
        BOARDS.add(board);
        setBoard();
    }
    private boolean isFirstTap = true;
    public void setReset(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!!!")
               .setMessage("Do you really want to reset Board???");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                BOARDS.clear();
                MOVED_PIECES.clear();
                final Board board = Board.standardBoard();
                BOARDS.add(board);
                isFirstTap = true;
                setBoard();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Aborted", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void move(View view){

        ImageView tappedView = (ImageView) view;
        int tappedViewTag = Integer.parseInt(tappedView.getTag().toString());
        GridLayout boardGrid = findViewById(R.id.gridLayoutChessboard);
        final Board board = BOARDS.get(BOARDS.size()-1);

        if (isFirstTap) {
            if (board.getTile(tappedViewTag).getTILE_STATUS() == Tile.TILE_STATUS.OCCUPIED){
                if (board.getTile(tappedViewTag).getPiece().getPieceAlliance() == board.getCurrentPlayer().getAlliance()){
                    final Piece piece = board.getTile(tappedViewTag).getPiece();
                    MOVED_PIECES.add(piece);
                    for (int i=0; i<piece.calculateLegalMoves(board).size(); i++){
                        int destinationTile = piece.calculateLegalMoves(board).get(i).getDestinationCoordinates();
                        ImageView destinationTileView = (ImageView) boardGrid.getChildAt(destinationTile);
                        destinationTileView.setImageResource(R.drawable.red);
                    }
                    isFirstTap = false;
                } else {
                    Toast.makeText(getApplicationContext(),"NOT YOUR TURN",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "NO PIECE HERE!!!", Toast.LENGTH_SHORT).show();
            }
        }
        if (!isFirstTap){
            final Piece selectedPiece = MOVED_PIECES.get(MOVED_PIECES.size()-1);
            for (int i=0; i<selectedPiece.calculateLegalMoves(board).size(); i++){
                if (selectedPiece.calculateLegalMoves(board).get(i).getDestinationCoordinates() == tappedViewTag){
                    final Move move = selectedPiece.calculateLegalMoves(board).get(i);
                    final Board boardAfterExecution = move.execute();
                    Toast.makeText(getApplicationContext(),boardAfterExecution.getCurrentPlayer().moveInfo(move).toString(),Toast.LENGTH_SHORT).show();
                    BOARDS.add(boardAfterExecution);
                    isFirstTap = true;
                    setBoard();
                    MOVED_PIECES.clear();
                    BOARDS.remove(0);
                    break;
                }
            }
        }
    }
    public void setBoard(){
        GridLayout chessBoardGrid = findViewById(R.id.gridLayoutChessboard);
        final Board boardForMapping = BOARDS.get(BOARDS.size()-1);
        for (int i=0; i<ChessUtils.NO_OF_TILES; i++){
            ImageView tileView = (ImageView) chessBoardGrid.getChildAt(i);
            Tile tileSelected = boardForMapping.getTile(i);
            if (tileSelected.getTILE_STATUS() == Tile.TILE_STATUS.OCCUPIED){
                Piece pieceOnTile = tileSelected.getPiece();
                if (pieceOnTile.getPieceAlliance() == Alliance.WHITE){
                    if (pieceOnTile instanceof King){
                        tileView.setImageResource(R.drawable.kw);
                    }
                    if (pieceOnTile instanceof Queen){
                        tileView.setImageResource(R.drawable.qw);
                    }
                    if (pieceOnTile instanceof Rook){
                        tileView.setImageResource(R.drawable.rw);
                    }
                    if (pieceOnTile instanceof Knight){
                        tileView.setImageResource(R.drawable.nw);
                    }
                    if (pieceOnTile instanceof Bishop){
                        tileView.setImageResource(R.drawable.bw);
                    }
                    if (pieceOnTile instanceof Pawn){
                        tileView.setImageResource(R.drawable.pw);
                    }
                } else if (pieceOnTile.getPieceAlliance() == Alliance.BLACK){
                    if (pieceOnTile instanceof King){
                        tileView.setImageResource(R.drawable.kb);
                    }
                    if (pieceOnTile instanceof Queen){
                        tileView.setImageResource(R.drawable.qb);
                    }
                    if (pieceOnTile instanceof Rook){
                        tileView.setImageResource(R.drawable.rb);
                    }
                    if (pieceOnTile instanceof Knight){
                        tileView.setImageResource(R.drawable.nb);
                    }
                    if (pieceOnTile instanceof Bishop){
                        tileView.setImageResource(R.drawable.bb);
                    }
                    if (pieceOnTile instanceof Pawn){
                        tileView.setImageResource(R.drawable.pb);
                    }
                }
            } else {
                tileView.setImageResource(0);
            }
        }
    }
}