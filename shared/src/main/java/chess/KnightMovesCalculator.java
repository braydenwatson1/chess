package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> myCollection = new HashSet<>();
        ChessPosition myPos = position;
        ChessBoard myBoard = board;

        //8 directions
        //UPRIGHT
        ChessPosition tryPos = new ChessPosition(myPos.getRow() + 2, myPos.getColumn() + 1);
        if (!(tryPos.getColumn() > 8 || tryPos.getRow() > 8 || tryPos.getColumn() < 1 || tryPos.getRow() < 1)) {
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
            };
        };
        //RIGHTUP
        ChessPosition tryPos2 = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 2);
        if (!(tryPos2.getColumn() > 8 || tryPos2.getRow() > 8 || tryPos2.getColumn() < 1 || tryPos2.getRow() < 1)) {
            if (myBoard.getPiece(tryPos2) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos2, null));
            } else if (myBoard.getPiece(tryPos2).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos2, null));
            };
        };
        //RIGHTDOWN
        ChessPosition tryPos3 = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 2);
        if (!(tryPos3.getColumn() > 8 || tryPos3.getRow() > 8 || tryPos3.getColumn() < 1 || tryPos3.getRow() < 1)) {
            if (myBoard.getPiece(tryPos3) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos3, null));
            } else if (myBoard.getPiece(tryPos3).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos3, null));
            };
        };
        //DOWNRIGHT
        ChessPosition tryPos4 = new ChessPosition(myPos.getRow() -2 , myPos.getColumn() + 1);
        if (!(tryPos4.getColumn() > 8 || tryPos4.getRow() > 8 || tryPos4.getColumn() < 1 || tryPos4.getRow() < 1)) {
            if (myBoard.getPiece(tryPos4) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos4, null));
            } else if (myBoard.getPiece(tryPos4).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos4, null));
            };
        };
        //DOWNLEFT
        ChessPosition tryPos5 = new ChessPosition(myPos.getRow() - 2, myPos.getColumn() - 1);
        if (!(tryPos5.getColumn() > 8 || tryPos5.getRow() > 8 || tryPos5.getColumn() < 1 || tryPos5.getRow() < 1)) {
            if (myBoard.getPiece(tryPos5) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos5, null));
            } else if (myBoard.getPiece(tryPos5).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos5, null));
            };
        };
        //LEFTDOWN
        ChessPosition tryPos6 = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 2);
        if (!(tryPos6.getColumn() > 8 || tryPos6.getRow() > 8 || tryPos6.getColumn() < 1 || tryPos6.getRow() < 1)) {
            if (myBoard.getPiece(tryPos6) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos6, null));
            } else if (myBoard.getPiece(tryPos6).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos6, null));
            };
        };
        //LEFTUP
        ChessPosition tryPos7 = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 2);
        if (!(tryPos7.getColumn() > 8 || tryPos7.getRow() > 8 || tryPos7.getColumn() < 1 || tryPos7.getRow() < 1)) {
            if (myBoard.getPiece(tryPos7) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos7, null));
            } else if (myBoard.getPiece(tryPos7).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos7, null));
            };
        };
        //UPLEFT
        ChessPosition tryPos8 = new ChessPosition(myPos.getRow() + 2, myPos.getColumn() - 1);
        if (!(tryPos8.getColumn() > 8 || tryPos8.getRow() > 8 || tryPos8.getColumn() < 1 || tryPos8.getRow() < 1)) {
            if (myBoard.getPiece(tryPos8) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos8, null));
            } else if (myBoard.getPiece(tryPos8).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos8, null));
            };
        };

        return myCollection;
    }
}
