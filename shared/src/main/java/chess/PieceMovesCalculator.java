package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition position);
}
