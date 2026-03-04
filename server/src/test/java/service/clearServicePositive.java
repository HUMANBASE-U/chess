package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDao;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class clearServicePositive {
    private DataAccess dao;
    private ClearService clearMethod;

    @BeforeEach
    void setUp(){
    dao = new MemoryDao();
    clearMethod = new ClearService(dao);
    }

    @Test
    @DisplayName("clear all, i will check u,a,g")
    void setClearMethod() throws DataAccessException {
        dao.createUser(new UserData("eason", "123", "123@gmail.com"));
        dao.createAuth(new AuthData("&s2", "eason"));
        dao.createGame(new GameData(0,"eason", "ajax", "badday", new ChessGame()));

        dao.clear();

        assertNull
    }

}
