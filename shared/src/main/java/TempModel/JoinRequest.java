package TempModel;

import chess.ChessGame;

import java.util.Collection;

public record JoinRequest(int GameID, ChessGame.TeamColor playColor, String authToken) {}