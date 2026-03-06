package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutNegativeTests {
    private DataAccess dao;
    private UserService userService;

    @BeforeEach
    void setUp(){
        dao = new MemoryDao();
        userService = new UserService(dao);
    }

    @Test
    @DisplayName("Wrong Logout process")
    void setRegisterservice() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        assertThrows(UnauthorizedException.class,
        ()-> userService.logout(new RR.LogoutRequest("s")));
    }
}
