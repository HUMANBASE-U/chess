package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateUserNegativeTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
        dao.createUser(new UserData("s","s","s"));
    }

    @Test
    void dupicatedUsers(){
        assertThrows(DataAccessException.class,() -> {
            dao.createUser(new UserData("s","s","s"));
        });
    }
}
