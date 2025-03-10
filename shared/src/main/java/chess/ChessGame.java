package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
    board = new ChessBoard();
    board.resetBoard();

    teamTurn = TeamColor.WHITE;
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
        //get the list of piece moves. this is done in pieceMoves from phase 0
        if (myPiece == null) {
            return new HashSet<>();
        }
        Collection<ChessMove> possibleMoves = myPiece.pieceMoves(board,startPosition);
        HashSet<ChessMove> VALIDMoves = new HashSet<>();

        //first see, am i already in check?
        TeamColor myColor = myPiece.getTeamColor();
        if (isInCheck(myColor)) {
            //is it checkmate?
            if (isInCheckmate(myColor)) {
                HashSet<ChessMove> emptyC2 = new HashSet<>();
                return emptyC2;
            }
            //assuming its NOT checkmate...
                if (myPiece == null) {
                    return new HashSet<>();
                }
                for ( ChessMove tryThisMove : myPiece.pieceMoves(board, startPosition )) {
                    //try to make the move
                    ChessPiece tempSPiece = board.getPiece(tryThisMove.getStartPosition());
                    ChessPiece tempEPiece = board.getPiece(tryThisMove.getEndPosition());

                    board.addPiece(tryThisMove.getStartPosition(), null);
                    board.addPiece(tryThisMove.getEndPosition(), tempSPiece);

                    if (!isInCheck(myColor)) {
                        //it works, add it to the list of valid moves
                        VALIDMoves.add(tryThisMove);
                    }

                    //put the pieces back
                    board.addPiece(tryThisMove.getStartPosition(), tempSPiece);
                    board.addPiece(tryThisMove.getEndPosition(), tempEPiece);
            }


        }
        //i think this is redundant so i am testing that by commenting it out
        else {
            //if not immediatley in check, then
            //remove the piece and see if you get in check by moving it
            for ( ChessMove tryThisMove : myPiece.pieceMoves(board, startPosition )) {
                //try to make the move
                ChessPiece tempSPiece = board.getPiece(tryThisMove.getStartPosition());
                ChessPiece tempEPiece = board.getPiece(tryThisMove.getEndPosition());

                board.addPiece(tryThisMove.getStartPosition(), null);
                board.addPiece(tryThisMove.getEndPosition(), tempSPiece);

                if (!isInCheck(myColor)) {
                    //it works, add it to the list of valid moves
                    VALIDMoves.add(tryThisMove);
                }

                //put the pieces back
                board.addPiece(tryThisMove.getStartPosition(), tempSPiece);
                board.addPiece(tryThisMove.getEndPosition(), tempEPiece);
            }

        }

        //return the result
        return VALIDMoves;
    }

    /**
     * Makes a move in a chess game
     *
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessMove tryMove = move;
        ChessPosition sPos = tryMove.getStartPosition();
        ChessPosition ePos = tryMove.getEndPosition();
        if (board.getPiece(sPos) == null) {
            throw new InvalidMoveException("no piece to move here");
        }
        TeamColor myColor = board.getPiece(sPos).getTeamColor();

        TeamColor whoseTurn = getTeamTurn();

        if (whoseTurn != myColor) {
            throw new InvalidMoveException("Not your turn");
        }

        //is the move valid?
        Collection<ChessMove> valMoves = validMoves(sPos);
        if (valMoves.contains(move)) {
            //make the move
            ChessPiece tempSPiece = board.getPiece(move.getStartPosition());

            board.addPiece(move.getStartPosition(), null);
            if (move.getPromotionPiece() == null) {
                board.addPiece(move.getEndPosition(), tempSPiece);
            }
            else {
                board.addPiece(move.getEndPosition(), new ChessPiece(myColor,move.getPromotionPiece()));
            }

            //switch team turn
            if (getTeamTurn() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            }
            else {
                setTeamTurn(TeamColor.WHITE);
            }
        }
        else {
            throw new InvalidMoveException("Not a valid move");
        }
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

        for (int c = 1; c < 9; c++) {
            for (int r = 1; r < 9; r++) {
                ChessPosition tryPos = new ChessPosition(r,c);
                if (board.getPiece(tryPos) == null) { continue; }
                if (board.getPiece(tryPos).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(tryPos).getTeamColor() == teamColor) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
        }

        ChessPosition kingLocation = new ChessPosition(kingRow, kingCol);

        //is he in check?
        for (int c = 1; c < 9; c++) {
            for (int r = 1; r < 9; r++) {
                ChessPosition tryPos = new ChessPosition(r,c);
                ChessPiece tryPiece = board.getPiece(tryPos);
                if (board.getPiece(tryPos) == null) { continue; }
                //if the piece at r,c is NOT our team color (then go on to check if we are in check)
                if (tryPiece.getTeamColor() != teamColor) {
                    for ( ChessMove possibleMove : tryPiece.pieceMoves(board, tryPos )) {
                        if (possibleMove.getEndPosition().equals(kingLocation)) {
                            //king IS IN CHECK
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            //stalemate or not, it ain't checkmate if your not in check...
            return false;
        }
        else{
            for (int c = 1; c < 9; c++) {
                for (int r = 1; r < 9; r++) {
                    ChessPosition tryPos = new ChessPosition(r, c);
                    ChessPiece tryPiece = board.getPiece(tryPos);
                    if (tryPiece == null) {
                        continue;
                    }
                    //if the piece at r,c is our team color (then we see if we can get ourselves out of check my moving it)
                    if (tryPiece.getTeamColor() == teamColor) {
                        for (ChessMove tryThisMove : tryPiece.pieceMoves(board, tryPos)) {
                            //try to make the move
                            ChessPiece tempSPiece = board.getPiece(tryThisMove.getStartPosition());
                            ChessPiece tempEPiece = board.getPiece(tryThisMove.getEndPosition());

                            board.addPiece(tryThisMove.getStartPosition(), null);
                            board.addPiece(tryThisMove.getEndPosition(), tempSPiece);

                            if (!isInCheck(teamColor)) {
                                //we got out of check, so not mate (yet)
                                //put the pieces back
                                board.addPiece(tryThisMove.getStartPosition(), tempSPiece);
                                board.addPiece(tryThisMove.getEndPosition(), tempEPiece);
                                return false;
                            }
                            board.addPiece(tryThisMove.getStartPosition(), tempSPiece);
                            board.addPiece(tryThisMove.getEndPosition(), tempEPiece);
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)) {
            return false;
        }
        else {
            //scan the board, if we can't move anywhere, then we are in stalemate
            for (int c = 1; c < 9; c++) {
                for (int r = 1; r < 9; r++) {
                    ChessPosition tryPos = new ChessPosition(r, c);
                    ChessPiece tryPiece = board.getPiece(tryPos);
                    if (tryPiece == null) {
                        continue;
                    }
                    //if the piece at r,c is our team color
                    if (tryPiece.getTeamColor() == teamColor) {
                        Collection<ChessMove> myMoves = validMoves(tryPos);
                        if (!myMoves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
