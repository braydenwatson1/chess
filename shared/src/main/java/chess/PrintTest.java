package chess;

public class PrintTest {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();  // Initialize the board with pieces
        System.out.println(board);  // Print the chessboard to see the result
    }
}