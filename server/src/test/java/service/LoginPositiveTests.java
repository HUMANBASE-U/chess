package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginPositiveTests {
    private DataAccess dao;
    private UserService userService;

    @BeforeEach
    void setUp(){
        dao = new MemoryDao();
        userService = new UserService(dao);
    }

    @Test
    @DisplayName("Normal Login process")
    void setRegisterservice() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        //先注册
        userService.register(new RR.RegisterRequest("s", "s", "s"));
        RR.LoginResult result = userService.login(new RR.LoginRequest("s", "s"));

        //找authToken , 回来名字，我也比较一下
        assertNotNull(result.authToken());
        assertEquals("s", result.username());
    }
}
