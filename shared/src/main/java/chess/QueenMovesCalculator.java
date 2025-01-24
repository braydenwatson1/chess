package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> myCollection = new HashSet<>();
        ChessPosition myPos = position;
        ChessBoard myBoard = board;

        //4 ROOK DIRECTIONS
        boolean laneOpen = true;
        //UP
        for (int i = 1; i < 9; i++) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() + i, myPos.getColumn());
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                laneOpen = false;
            }
            if (!laneOpen) { break; }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
                continue;
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
                laneOpen = false;
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
                laneOpen = false;
            }
        };
        //RIGHT
        laneOpen = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow(), myPos.getColumn() + i);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                laneOpen = false;
            }
            if (!laneOpen) { break; }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
                continue;
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
                laneOpen = false;
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
                laneOpen = false;
            }
        };
        //DOWN
        laneOpen = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() - i, myPos.getColumn());
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                laneOpen = false;
            }
            if (!laneOpen) { break; }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
                continue;
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
                laneOpen = false;
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
                laneOpen = false;
            }
        };
        //LEFT
        laneOpen = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow(), myPos.getColumn() - i);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                laneOpen = false;
            }
            if (!laneOpen) { break; }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
                continue;
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
                laneOpen = false;
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
                laneOpen = false;
            }
        };

        //4 BISHOP DIRECTIONS
        laneOpen = true;
        //UPRIGHT
        for (int i = 1; i < 9; i++) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() + i, myPos.getColumn() + i);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                laneOpen = false;
            }
            if (!laneOpen) { break; }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
                continue;
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
                laneOpen = false;
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
                laneOpen = false;
            }
        };
        //DOWNRIGHT
        laneOpen = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() - i, myPos.getColumn() + i);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                laneOpen = false;
            }
            if (!laneOpen) { break; }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
                continue;
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
                laneOpen = false;
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
                laneOpen = false;
            }
        };
        //DOWNLEFT
        laneOpen = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() - i, myPos.getColumn() - i);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                laneOpen = false;
            }
            if (!laneOpen) { break; }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
                continue;
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
                laneOpen = false;
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
                laneOpen = false;
            }
        };
        //UPLEFT
        laneOpen = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() + i, myPos.getColumn() - i);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                laneOpen = false;
            }
            if (!laneOpen) { break; }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
                continue;
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
                laneOpen = false;
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
                laneOpen = false;
            }
        };

        return myCollection;
    };
