package com.example.shatranj;

public class MoveTransition {

    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    MoveTransition(Board board, Move move, MoveStatus moveStatus){
        this.transitionBoard = board;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    @Override
    public String toString() {
        return move.isAttackMove() ? " Attack Move ":" Normal Move ";
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getTransitionBoard(){
        return this.transitionBoard;
    }
}
enum MoveStatus{
    DONE{
        @Override
        public boolean isDone(){
            return true;
        }
    },
    ILLEGAL_MOVE{
        @Override
        public boolean isDone() {
            return false;
        }
    },
    PLAYER_ON_CHECK{
        @Override
        public boolean isDone() {
            return false;
        }
    };
    public abstract boolean isDone();
}
