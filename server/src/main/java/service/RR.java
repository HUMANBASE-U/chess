package service;

public class RR {
    //REG
    public record RegisterRequest(String username, String password, String email) {}
    public record RegisterResult(String username, String authToken) {}

    //LOG
    public record LoginRequest(String username, String password) {}
    public record LoginResult(String username, String authToken) {}
    public record LogoutRequest(String authToken) {}
}
