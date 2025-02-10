package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    private boolean inPlay;

    public ChessGame() {
    board = new ChessBoard();
    board.resetBoard();

    teamTurn = TeamColor.WHITE;
    inPlay = true;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {return teamTurn;}

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //get piece in the position
        ChessPiece myPiece = board.getPiece(startPosition);
            //if its null in the position, there are no valid moves
            if (myPiece == null) {
                return null;
            }
        //get the list of piece moves. this is done in pieceMoves from phase 0
        Collection<ChessMove> possibleMoves = myPiece.pieceMoves(board,startPosition);
        //remove check




        //return the result
        return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //find the king
        int kingRow = 0;
        int kingCol = 0;

        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                ChessPosition tryPos = new ChessPosition(r,c);
                if (board.getPiece(tryPos).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(tryPos).getTeamColor() != teamColor) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
        }

        ChessPosition kingLocation = new ChessPosition(kingRow, kingCol);

        //is he in check?
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                //if the piece at r,c is NOT our team color (then go on to check if we are in check)
                if (board.getPiece(new ChessPosition(r,c)).getTeamColor() != teamColor) {
                    
                }
            }
        }


    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
