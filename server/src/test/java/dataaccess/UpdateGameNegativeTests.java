package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateGameNegativeTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }


    @Test
    void updateGameInWrongParameter() throws DataAccessException, SQLException {
        int gameID = dao.createGame(
                new GameData(0,
                        null,null,
                        "epic_game",
                        new ChessGame()));


        assertThrows(DataAccessException.class,() -> {
            dao.updateGame(
                    new GameData(gameID,
                            "ddd",null,
                            null,
                            new ChessGame()));
        });
    }
}