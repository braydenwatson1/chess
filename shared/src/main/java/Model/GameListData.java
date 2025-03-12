package Model;

import chess.ChessGame;

public record GameListData(int gameID, String whiteUsername, String blackUsername, String gameName) {}