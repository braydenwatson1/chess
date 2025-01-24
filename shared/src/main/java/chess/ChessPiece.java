package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    this.color = pieceColor;
    this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public String toString() {
        // First, get the symbol for the piece type (uppercase for white, lowercase for black)
        String pieceSymbol = "";
        switch (type) {
            case KING:
                pieceSymbol = "K";
                break;
            case QUEEN:
                pieceSymbol = "Q";
                break;
            case ROOK:
                pieceSymbol = "R";
                break;
            case KNIGHT:
                pieceSymbol = "N";
                break;
            case BISHOP:
                pieceSymbol = "B";
                break;
            case PAWN:
                pieceSymbol = "P";
                break;
        }
        //uppercase if piece is white. lowercase if piece is black
        return (color == ChessGame.TeamColor.WHITE) ? pieceSymbol : pieceSymbol.toLowerCase();
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator movesCalculator;

        switch (this.getPieceType()) {
            case KING:
                movesCalculator = new KingMovesCalculator();
                break;
            case QUEEN:
                movesCalculator = new QueenMovesCalculator();
                break;
            case BISHOP:
                movesCalculator = new BishopMovesCalculator();
                break;
            case KNIGHT:
                movesCalculator = new KnightMovesCalculator();
                break;
            case ROOK:
                movesCalculator = new RookMovesCalculator();
                break;
            case PAWN:
                movesCalculator = new PawnMovesCalculator();
                break;
            default:
                throw new IllegalArgumentException("Invalid piece type: " + this.getPieceType());
        }

        return movesCalculator.PieceMoves(board, myPosition);
    }

}
