package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetGamePositiveTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void GetGamePositive() throws DataAccessException {
        int gameId = dao.createGame(
                        new GameData(0,
                        null,null,
                        "epic_game",
                        new ChessGame()));

        assertNotNull(dao.getGame(gameId));
    }

}