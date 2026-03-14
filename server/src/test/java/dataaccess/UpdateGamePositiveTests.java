package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateGamePositiveTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void updateGame() throws DataAccessException {

        int gameID = dao.createGame(
                        new GameData(0,
                        null,null,
                        "epic_game",
                        new ChessGame()));

        dao.updateGame(
                        new GameData(gameID,
                        "ddd",null,
                        "epic_game",
                        new ChessGame()));


        assertDoesNotThrow(() -> {
            try (var conn = DatabaseManager.getConnection();
                 var ps = conn.prepareStatement("SELECT * FROM games WHERE game_id = ?");
                 ) {
                ps.setInt(1, gameID);
                var result = ps.executeQuery();
                assertTrue(result.next());

                assertEquals("ddd", result.getString("white_username"));
            }
        });
    }
}
