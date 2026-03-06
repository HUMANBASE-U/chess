package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegisterNegativeTests {
    private DataAccess dao;
    private UserService userService;

    @BeforeEach
    void setUp(){
        dao = new MemoryDao();
        userService = new UserService(dao);
    }

    @Test//negative need
    @DisplayName("wrong input , test for the BadRequestException")

    void setRegisterservice(){
        assertThrows(BadRequestException.class,
        () ->  userService.register(new RR.RegisterRequest(null, null, "S"))
        );
    }
}
