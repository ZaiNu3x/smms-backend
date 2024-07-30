package group.intelliboys.smms_backend.exceptions;

public class TokenBlacklistedException extends RuntimeException {
    public TokenBlacklistedException() {
    }

    public TokenBlacklistedException(String message) {
        super(message);
    }
}
