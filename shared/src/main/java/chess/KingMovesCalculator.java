package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> myCollection = new HashSet<>();
        ChessPosition myPosition = position;
        ChessBoard myBoard = board;

    };
}
