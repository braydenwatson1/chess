package TempModel;

import chess.ChessGame;

public record JoinRequest(int GameID, ChessGame.TeamColor playColor, String authToken) {}