package service;

public class ServiceException extends Exception {
    public ServiceException(String message) {

        super(message);
    }
}

public class AlreadyTakenException extends ServiceException {
    public AlreadyTakenException(String message) {super(message); }
}
public class BadRequestException extends ServiceException {
    public BadRequestException(String message) {super(message); }
}



class WrongPasswordException extends ServiceException {
    public WrongPasswordException(String message) {super(message); }
}
public class UnauthorizedException extends ServiceException {
    public UnauthorizedException(String message) {super(message); }
}