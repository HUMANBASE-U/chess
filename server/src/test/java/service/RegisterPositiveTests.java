package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegisterPositiveTests {
    private DataAccess dao;
    private UserService userService;

    @BeforeEach
    void setUp(){
        dao = new MemoryDao();
        userService = new UserService(dao);
    }

    @Test
    @DisplayName("Normal register process")
    void setRegisterservice() throws BadRequestException, DataAccessException, AlreadyTakenException {
        RR.RegisterResult result = userService.register(new RR.RegisterRequest("s", "s", "s"));

        assertNotNull(result.authToken());
    }

}
