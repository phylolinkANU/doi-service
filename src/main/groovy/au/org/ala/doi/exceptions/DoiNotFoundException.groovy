package au.org.ala.doi.exceptions

class DoiNotFoundException extends RuntimeException {

    String id

    DoiNotFoundException(String id) {
        super("$id not found")
        this.id = id
    }

    @Override
    Throwable fillInStackTrace() {
        return this
    }
}