package au.org.ala.doi.exceptions;

import org.springframework.validation.Errors;

class DoiValidationException extends Exception {

    final UUID uuid
    final String doi
    final Errors errors

    DoiValidationException(UUID uuid, String doi, Errors errors) {
        super(errors.toString())
        this.uuid = uuid
        this.doi = doi
        this.errors = errors
    }

    @Override
    Throwable fillInStackTrace() {
        return this
    }
}
