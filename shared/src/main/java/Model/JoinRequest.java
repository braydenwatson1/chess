package Model;

import chess.ChessGame;

public record JoinRequest(int GameID, ChessGame.TeamColor playColor, AuthData authData) {}