public class CertGenException extends Exception {
    public CertGenException() {
    }

    public CertGenException(String message) {
        super(message);
    }

    public CertGenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertGenException(Throwable cause) {
        super(cause);
    }

    public CertGenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
