package account;

public class UserExistException extends RuntimeException {

    public UserExistException(String message){
        super(message);
    }

}
