package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder myboard = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                if (piece != null) {
                    myboard.append(" " + piece.toString() + " ");  // Print piece
                } else {
                    myboard.append(" . ");  // Empty square
                }
            }
            myboard.append("\n");  // Newline after each row
        }
        return myboard.toString();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        ChessPiece result = board[position.getRow()-1][position.getColumn()-1];
        return result;
    }
    //remember to subtract one
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //if you call get piece and add piece then the subtracting one is already in place
        //white is on the bottom, black is on the top

        //white
        ChessPiece wR1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece wN1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece wB1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece wQ = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece wK = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece wB2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece wN2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece wR2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece wP1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wP2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wP3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wP4 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wP5 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wP6 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wP7 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wP8 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        //add white power pieces to the board
        addPiece(new ChessPosition(1,1),wR1);
        addPiece(new ChessPosition(1,2),wN1);
        addPiece(new ChessPosition(1,3),wB1);
        addPiece(new ChessPosition(1,4),wQ);
        addPiece(new ChessPosition(1,5),wK);
        addPiece(new ChessPosition(1,6),wB2);
        addPiece(new ChessPosition(1,7),wN2);
        addPiece(new ChessPosition(1,8),wR2);
        //add white pawns
        addPiece(new ChessPosition(2,1),wP1);
        addPiece(new ChessPosition(2,2),wP2);
        addPiece(new ChessPosition(2,3),wP3);
        addPiece(new ChessPosition(2,4),wP4);
        addPiece(new ChessPosition(2,5),wP5);
        addPiece(new ChessPosition(2,6),wP6);
        addPiece(new ChessPosition(2,7),wP7);
        addPiece(new ChessPosition(2,8),wP8);


        //black
        ChessPiece bR1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece bN1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece bB1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece bQ = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece bK = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece bB2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece bN2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece bR2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece bP1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece bP2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece bP3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece bP4 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece bP5 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece bP6 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece bP7 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece bP8 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        //add black power pieces to the board
        addPiece(new ChessPosition(8,1),bR1);
        addPiece(new ChessPosition(8,2),bN1);
        addPiece(new ChessPosition(8,3),bB1);
        addPiece(new ChessPosition(8,4),bQ);
        addPiece(new ChessPosition(8,5),bK);
        addPiece(new ChessPosition(8,6),bB2);
        addPiece(new ChessPosition(8,7),bN2);
        addPiece(new ChessPosition(8,8),bR2);
        //add black pawns
        addPiece(new ChessPosition(7,1),bP1);
        addPiece(new ChessPosition(7,2),bP2);
        addPiece(new ChessPosition(7,3),bP3);
        addPiece(new ChessPosition(7,4),bP4);
        addPiece(new ChessPosition(7,5),bP5);
        addPiece(new ChessPosition(7,6),bP6);
        addPiece(new ChessPosition(7,7),bP7);
        addPiece(new ChessPosition(7,8),bP8);
    }
}
