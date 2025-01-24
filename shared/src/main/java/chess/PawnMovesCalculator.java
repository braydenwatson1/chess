package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> myCollection = new HashSet<>();
        ChessPosition myPos = position;
        ChessBoard myBoard = board;

        //WHITE TEAM
        if (myBoard.getPiece(myPos) != null && myBoard.getPiece(myPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
            boolean front_open = false;
            //FORWARD MOVEMENT
            ChessPosition tryPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn());
            if (!(tryPos.getColumn() > 8 || tryPos.getRow() > 8 || tryPos.getColumn() < 1 || tryPos.getRow() < 1)) {
                if (myBoard.getPiece(tryPos) == null) {
                    //square is open
                    if (tryPos.getRow() != 8) {
                        myCollection.add(new ChessMove(myPos, tryPos, null));
                    } else {
                        myCollection.add(new ChessMove(myPos, tryPos, ChessPiece.PieceType.BISHOP));
                        myCollection.add(new ChessMove(myPos, tryPos, ChessPiece.PieceType.KNIGHT));
                        myCollection.add(new ChessMove(myPos, tryPos, ChessPiece.PieceType.ROOK));
                        myCollection.add(new ChessMove(myPos, tryPos, ChessPiece.PieceType.QUEEN));
                    }
                    front_open = true;
                }
            }
            //FIRST TURN - FORWARD TWICE
            if (myPos.getRow() == 2 && front_open) {
                ChessPosition tryPos2 = new ChessPosition(myPos.getRow() + 2, myPos.getColumn());
                if (!(tryPos2.getColumn() > 8 || tryPos2.getRow() > 8 || tryPos2.getColumn() < 1 || tryPos2.getRow() < 1)) {
                    if (myBoard.getPiece(tryPos2) == null) {
                        //square is open
                        myCollection.add(new ChessMove(myPos, tryPos2, null));
                    }
                }
            }
            //OPTION TO KILL RIGHT
            ChessPosition tryPos3 = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1);
            if (!(tryPos3.getColumn() > 8 || tryPos3.getRow() > 8 || tryPos3.getColumn() < 1 || tryPos3.getRow() < 1)) {
                if (myBoard.getPiece(tryPos3) != null && myBoard.getPiece(tryPos3).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    //square has enemy
                    if (tryPos3.getRow() != 8) {
                        myCollection.add(new ChessMove(myPos, tryPos3, null));
                    } else {
                        myCollection.add(new ChessMove(myPos, tryPos3, ChessPiece.PieceType.BISHOP));
                        myCollection.add(new ChessMove(myPos, tryPos3, ChessPiece.PieceType.KNIGHT));
                        myCollection.add(new ChessMove(myPos, tryPos3, ChessPiece.PieceType.ROOK));
                        myCollection.add(new ChessMove(myPos, tryPos3, ChessPiece.PieceType.QUEEN));
                    }
                }
            }
            //OPTION TO KILL LEFT
            ChessPosition tryPos4 = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1);
            if (!(tryPos4.getColumn() > 8 || tryPos4.getRow() > 8 || tryPos4.getColumn() < 1 || tryPos4.getRow() < 1)) {
                if (myBoard.getPiece(tryPos4) != null && myBoard.getPiece(tryPos4).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    //square has enemy
                    if (tryPos4.getRow() != 8) {
                        myCollection.add(new ChessMove(myPos, tryPos4, null));
                    } else {
                        myCollection.add(new ChessMove(myPos, tryPos4, ChessPiece.PieceType.BISHOP));
                        myCollection.add(new ChessMove(myPos, tryPos4, ChessPiece.PieceType.KNIGHT));
                        myCollection.add(new ChessMove(myPos, tryPos4, ChessPiece.PieceType.ROOK));
                        myCollection.add(new ChessMove(myPos, tryPos4, ChessPiece.PieceType.QUEEN));
                    }
                }
            }
        }

        //BLACK TEAM
        if (myBoard.getPiece(myPos) != null && myBoard.getPiece(myPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
            boolean front_open = false;
            //FORWARD MOVEMENT
            ChessPosition tryPos = new ChessPosition(myPos.getRow() - 1, myPos.getColumn());
            if (!(tryPos.getColumn() > 8 || tryPos.getRow() > 8 || tryPos.getColumn() < 1 || tryPos.getRow() < 1)) {
                if (myBoard.getPiece(tryPos) == null) {
                    //square is open
                    if (tryPos.getRow() != 1) {
                        myCollection.add(new ChessMove(myPos, tryPos, null));
                    } else {
                        myCollection.add(new ChessMove(myPos, tryPos, ChessPiece.PieceType.BISHOP));
                        myCollection.add(new ChessMove(myPos, tryPos, ChessPiece.PieceType.KNIGHT));
                        myCollection.add(new ChessMove(myPos, tryPos, ChessPiece.PieceType.ROOK));
                        myCollection.add(new ChessMove(myPos, tryPos, ChessPiece.PieceType.QUEEN));
                    }
                    front_open = true;
                }
            }
            //FIRST TURN - FORWARD TWICE
            if (myPos.getRow() == 7 && front_open) {
                ChessPosition tryPos2 = new ChessPosition(myPos.getRow() - 2, myPos.getColumn());
                if (!(tryPos2.getColumn() > 8 || tryPos2.getRow() > 8 || tryPos2.getColumn() < 1 || tryPos2.getRow() < 1)) {
                    if (myBoard.getPiece(tryPos2) == null) {
                        //square is open
                        myCollection.add(new ChessMove(myPos, tryPos2, null));
                    }
                }
            }
            //OPTION TO KILL RIGHT
            ChessPosition tryPos3 = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1);
            if (!(tryPos3.getColumn() > 8 || tryPos3.getRow() > 8 || tryPos3.getColumn() < 1 || tryPos3.getRow() < 1)) {
                if (myBoard.getPiece(tryPos3) != null && myBoard.getPiece(tryPos3).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    //square has enemy
                    if (tryPos3.getRow() != 1) {
                        myCollection.add(new ChessMove(myPos, tryPos3, null));
                    } else {
                        myCollection.add(new ChessMove(myPos, tryPos3, ChessPiece.PieceType.BISHOP));
                        myCollection.add(new ChessMove(myPos, tryPos3, ChessPiece.PieceType.KNIGHT));
                        myCollection.add(new ChessMove(myPos, tryPos3, ChessPiece.PieceType.ROOK));
                        myCollection.add(new ChessMove(myPos, tryPos3, ChessPiece.PieceType.QUEEN));
                    }
                }
            }
            //OPTION TO KILL LEFT
            ChessPosition tryPos4 = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1);
            if (!(tryPos4.getColumn() > 8 || tryPos4.getRow() > 8 || tryPos4.getColumn() < 1 || tryPos4.getRow() < 1)) {
                if (myBoard.getPiece(tryPos4) != null && myBoard.getPiece(tryPos4).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    //square has enemy
                    if (tryPos4.getRow() != 1) {
                        myCollection.add(new ChessMove(myPos, tryPos4, null));
                    } else {
                        myCollection.add(new ChessMove(myPos, tryPos4, ChessPiece.PieceType.BISHOP));
                        myCollection.add(new ChessMove(myPos, tryPos4, ChessPiece.PieceType.KNIGHT));
                        myCollection.add(new ChessMove(myPos, tryPos4, ChessPiece.PieceType.ROOK));
                        myCollection.add(new ChessMove(myPos, tryPos4, ChessPiece.PieceType.QUEEN));
                    }
                }
            }
        }
        return myCollection;
    }
}