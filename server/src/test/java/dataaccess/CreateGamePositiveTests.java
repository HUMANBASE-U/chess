package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateGamePositiveTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void CreateGame() throws DataAccessException {

        dao.createGame(
                new GameData(0,
                null,null,
                "epic_game",
                new ChessGame()));

        assertDoesNotThrow(() -> {
            try (var conn = DatabaseManager.getConnection();
                 var ps = conn.prepareStatement("SELECT * FROM games");
                 var result = ps.executeQuery()) {
                assertTrue(result.next());
            }
        });
    }
}