package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ListGamePositiveTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        int gameId = dao.createGame(
                new GameData(0,
                        null,null,
                        "epic_game",
                        new ChessGame()));

        assertNotNull(dao.listGames());
        assertEquals(1, dao.listGames().size());
    }

}