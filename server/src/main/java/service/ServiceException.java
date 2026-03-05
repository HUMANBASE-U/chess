package service;

public class ServiceException extends Exception {
    public ServiceException(String message) {

        super(message);
    }
}


class WrongPasswordException extends ServiceException {
    public WrongPasswordException(String message) {super(message); }
}
