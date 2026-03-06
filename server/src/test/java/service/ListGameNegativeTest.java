package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListGameNegativeTest {
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
        assertThrows(UnauthorizedException.class,
                () -> gameService.listGame(new RR.ListGameRequest("sssssssSSS"))
        );
    }
}
