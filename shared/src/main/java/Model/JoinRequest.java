package Model;

import chess.ChessGame;

public record JoinRequest(String GameID, ChessGame.TeamColor playColor, String authToken) {}