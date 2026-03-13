package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateGameNegativeTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void createGameInWrongParameter() throws DataAccessException {

        assertThrows(DataAccessException.class,() -> {
            dao.createGame(
                    new GameData(0,
                            null,null,
                            null,
                            new ChessGame()));
        });
    }
}