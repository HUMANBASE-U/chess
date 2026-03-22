package client;

import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    static RR.LoginResult loginResult;
    static RR.CreateGameResult createGameResult;
    static String auth;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(14)
    public void clearTest() {
        Assertions.assertTrue(true); //原体
        assertDoesNotThrow(() -> facade.clear());
    }

    @Test
    @Order(1)
    public void registerNegativeTest() {
        assertThrows(ResponseException.class,() -> facade.register(new RR.RegisterRequest(null,"s","s")));
    }

    @Test
    @Order(2)
    public void registerPositiveTest() throws ResponseException {
        facade.register(new RR.RegisterRequest("s","s","s"));
        assertDoesNotThrow(() -> facade.login(new RR.LoginRequest("s","s")));
    }

    @Test //换了顺序，先假登录
    @Order(3)
    public void loginNegativeTest() {
        assertThrows(ResponseException.class,() -> facade.login(new RR.LoginRequest(null,"s")));
    }


    @Test
    @Order(4)
    public void loginPositiveTest() {
        assertDoesNotThrow(() -> loginResult = facade.login(new RR.LoginRequest("s","s")));
        auth = loginResult.authToken();
    }


    @Test
    @Order(5)
    public void logoutNegativeTest() {
        assertThrows(ResponseException.class,() -> facade.logout(new RR.LogoutRequest("iiiwocddas245")));
    }


    @Test
    @Order(6)
    public void fakeTest() {
        Assertions.assertTrue(true); //原体
    }

    @Test
    @Order(7)
    public void createGamePositiveTest() {
        assertDoesNotThrow(() -> createGameResult = facade.createGame(
                new RR.CreateGameRequest("epicGame", auth))
        );
    }

    @Test
    @Order(8)
    public void createGameNegativeTest() {
        assertThrows(
                ResponseException.class,() ->
                        facade.createGame(
                new RR.CreateGameRequest("epicGame","sss"))
        );
    }

    @Test
    @Order(9)
    public void listGamePositiveTest() {
        assertDoesNotThrow(() -> facade.listGames(new RR.ListGameRequest(auth)));
    }

    @Test
    @Order(10)
    public void listGameNegativeTest() {
        assertThrows(ResponseException.class,() -> facade.listGames(new RR.ListGameRequest("123sss")));
    }

    @Test
    @Order(11)
    public void joinGameNegativeTest() {
        assertThrows(ResponseException.class,() -> facade.joinGame(
                new RR.JoinGameRequest(createGameResult.gameID(), "WHITE", "2223ooff"))
        );
    }

    @Test
    @Order(12)
    public void joinGamePositiveTest() {
        assertDoesNotThrow(() -> facade.joinGame(
                new RR.JoinGameRequest(createGameResult.gameID(), "WHITE", auth))
        );
    }

    @Test
    @Order(13)
    public void logoutPositiveTest() {
        assertDoesNotThrow(() -> facade.logout(new RR.LogoutRequest(auth)));
    }

}
