package ui;

import chess.*;
import java.util.Collection;
import java.util.HashSet;
import static ui.EscapeSequences.*;

public class BoardPrint {

    ChessGame game;

    public BoardPrint(ChessGame game) {
        this.game = game;
    }

    public void updateGame(ChessGame game) {
        this.game = game;
    }

    public void printBoard(String teamColorString, ChessPosition selectedPosition) {

        ChessGame.TeamColor teamColor = null;
        if (teamColorString.equalsIgnoreCase("white")) {
            teamColor = ChessGame.TeamColor.WHITE;
        }
        else if (teamColorString.equalsIgnoreCase("black")) {
            teamColor = ChessGame.TeamColor.BLACK;
        }

        StringBuilder output = new StringBuilder();

        HashSet<ChessPosition> highlightedSquares = new HashSet<>();
        //is there a position selected?
        if (selectedPosition != null) {
            Collection<ChessMove> posMoves = game.validMoves(selectedPosition);
            if (posMoves != null) {
                for (ChessMove move : posMoves) {
                    ChessPosition ender = move.getEndPosition();
                    highlightedSquares.add(ender);
                }
            }
        }

        // If black, reverse so black faces forward
        if (teamColor == ChessGame.TeamColor.BLACK)
        {
// Print the row identifiers
            output.append("    a  b  c  d  e  f  g  h    \n");
            for (int rowNum = 8; rowNum > 0; rowNum--) {

                output.append(" " + rowNum + " ");

                for (int col = 1; col <= 8; col++) {
                    ChessPosition square = new ChessPosition(rowNum, col);
                    //square color:
                    boolean highlighted;
                    if (highlightedSquares.contains(square)) {
                        highlighted = true;
                    } else { highlighted = false; }
                    String bgColor = getSquareColor(rowNum, col, highlighted);
                    //Piece in square (including if its empty)
                    String result = getSquareColor(rowNum, col, highlighted);
                    String squareColor = "";
                    if (result.equals("light")){
                        squareColor = SET_BG_COLOR_LIGHT_GREY;
                    } else if (result.equals("dark")){
                        squareColor = SET_BG_COLOR_DARK_GREEN;
                    } else if (result.equals("highlight")) {
                        squareColor = SET_BG_COLOR_BLUE;
                    } else {
                        System.out.println("Error in background color setting. Contact admin");
                    }

                    output.append(squareColor);
                    output.append(getPiece(new ChessPosition(rowNum,col), game));
                    output.append(RESET_BG_COLOR + RESET_TEXT_COLOR); // Reset text formatting
                }
                output.append("\n");
            }
            // Print the row identifiers
            output.append("    a  b  c  d  e  f  g  h   \n");
        }
        else {
            // Print the row identifiers
            output.append("    a  b  c  d  e  f  g  h    \n");
            for (int rowNum = 8; rowNum > 0; rowNum--) {

                output.append(" " + rowNum + " ");

                for (int col = 1; col <= 8; col++) {
                    ChessPosition square = new ChessPosition(rowNum, col);
                    //square color:
                    boolean highlighted;
                    if (highlightedSquares.contains(square)) {
                        highlighted = true;
                    } else { highlighted = false; }
                    String bgColor = getSquareColor(rowNum, col, highlighted);
                    //Piece in square (including if its empty)
                    String result = getSquareColor(rowNum, col, highlighted);
                    String squareColor = "";
                    if (result.equals("light")){
                        squareColor = SET_BG_COLOR_LIGHT_GREY;
                    } else if (result.equals("dark")){
                        squareColor = SET_BG_COLOR_DARK_GREEN;
                    } else if (result.equals("highlight")) {
                        squareColor = SET_BG_COLOR_BLUE;
                    } else {
                        System.out.println("Error in background color setting. Contact admin");
                    }

                    output.append(squareColor);
                    output.append(getPiece(new ChessPosition(rowNum,col), game));
                    output.append(RESET_BG_COLOR + RESET_TEXT_COLOR); // Reset text formatting
                }
            output.append("\n");
            }
            // Print the row identifiers
            output.append("    a  b  c  d  e  f  g  h   \n");
        }

        output.append(RESET_BG_COLOR+RESET_TEXT_COLOR); // Reset text formatting
        System.out.println(output);
        System.out.printf("%s Turn to move.\n", game.getTeamTurn().toString());
    }

    private String getSquareColor(int row, int col, boolean highlighted) {
        if (highlighted) {
            return "highlight";
        }
        if (row % 2 == 0) {
            if (col % 2 == 0) {
                return "dark";
            } else {
                return "light";
            }
        } else {
            if (col % 2 == 0) {
                return "light";
            } else {
                return "dark";
            }
        }
    }

    private String getPiece(ChessPosition position, ChessGame game) {
        StringBuilder output = new StringBuilder();
        ChessPiece piece = game.getBoard().getPiece(position);
        if (piece == null) {
            output.append("   ");
        }
        else {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                output.append(SET_TEXT_COLOR_WHITE);
                switch (piece.getPieceType()) {
                    case KING -> output.append(" K ");
                    case QUEEN -> output.append(" Q ");
                    case ROOK -> output.append(" R ");
                    case KNIGHT -> output.append(" N ");
                    case BISHOP -> output.append(" B ");
                    case PAWN -> output.append(" P ");
                }
            }
            else {
                output.append(SET_TEXT_COLOR_BLACK);
                switch (piece.getPieceType()) {
                    case KING -> output.append(" K ");
                    case QUEEN -> output.append(" Q ");
                    case ROOK -> output.append(" R ");
                    case KNIGHT -> output.append(" N ");
                    case BISHOP -> output.append(" B ");
                    case PAWN -> output.append(" P ");
                }
            }
        }
        return output.toString();
    }
}
