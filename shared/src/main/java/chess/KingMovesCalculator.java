package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> myCollection = new HashSet<>();
        ChessPosition myPos = position;
        ChessBoard myBoard = board;

        //8 DIRECTIONS
        //UP
        while (1==1) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn());
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                break;
            }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
            };
            break;
        };
        //UPRIGHT
        while (1==1) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                break;
            }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
            };
            break;
        };
        //RIGHT
        while (1==1) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow(), myPos.getColumn() + 1);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                break;
            }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
            };
            break;
        };
        //DOWNRIGHT
        while (1==1) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                break;
            }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
            };
            break;
        };
        //DOWN
        while (1==1) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() - 1, myPos.getColumn());
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                break;
            }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
            };
            break;
        };
        //DOWNLEFT
        while (1==1) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                break;
            }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
            };
            break;
        };
        //LEFT
        while (1==1) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow(), myPos.getColumn() - 1);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                break;
            }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
            };
            break;
        };
        //UPLEFT
        while (1==1) {
            ChessPosition tryPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1);
            if (tryPos.getColumn() > 8 | tryPos.getRow() > 8 | tryPos.getColumn() < 1 | tryPos.getRow() < 1) {
                break;
            }
            if (myBoard.getPiece(tryPos) == null) {
                //square is open
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() != myBoard.getPiece(myPos).getTeamColor()) {
                //enemy piece is in way
                myCollection.add(new ChessMove(myPos, tryPos, null));
            } else if (myBoard.getPiece(tryPos).getTeamColor() == myBoard.getPiece(myPos).getTeamColor()) {
                //friendly piece is in way
            };
            break;
        };



        return myCollection;
    };
}
