package bt.edu.gcit.usermicroservice.exception;


public class AccountNotEnabledException extends RuntimeException {
    public AccountNotEnabledException(String message) {
        super(message);
    }
}