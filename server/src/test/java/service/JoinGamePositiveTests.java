package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGamePositiveTests {
    private DataAccess dao;
    private UserService userService;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        dao = new MemoryDao();
        userService = new UserService(dao);
        gameService = new GameService(dao);
    }

    @Test
    void createGameSuccess() throws Exception {
        //先注册
        String token = userService.register(new RR.RegisterRequest("S", "S", "S")).authToken();

        //再开一把游戏
        RR.CreateGameResult result = gameService.createGame(new RR.CreateGameRequest("S", token));

        gameService.joinGame(new RR.JoinGameRequest(result.gameID(),"WHITE",token));


        assertNotNull(dao.getGame(result.gameID()).whiteUsername() );
    }
}
