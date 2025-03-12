package Model;

import chess.ChessGame;

public record JoinRequest(String gameID, ChessGame.TeamColor playerColor, String authToken) {}