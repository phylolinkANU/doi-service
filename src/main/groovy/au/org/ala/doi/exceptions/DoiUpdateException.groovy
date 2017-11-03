package au.org.ala.doi.exceptions

class DoiUpdateException extends RuntimeException {

    DoiUpdateException(String message) {
        super(message)
    }

    @Override
    Throwable fillInStackTrace() {
        return this
    }
}
