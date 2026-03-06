package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JoinGameNegativeTests {
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
        assertThrows(BadRequestException.class,
                ()-> gameService.joinGame(new RR.JoinGameRequest(10010,"WHITE","SSSSS"))
        );
    }
}
