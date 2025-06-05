package com.floss83.javaswitch.iso8583;

/**
 * Exception thrown when an error occurs during parsing of an ISO 8583 message.
 * <p>
 * This exception is used to signal invalid message format, malformed fields,
 * unsupported features, or unexpected data encountered during parsing.
 * </p>
 *
 * It supports fine-grained exception handling in upstream components,
 * ensuring robust error logging and recovery.
 * </p>
 * 
 * @author Gracemann365
 * @since 1.0
 */
public class Iso8583ParseException extends Exception {

    /**
     * Constructs a new Iso8583ParseException with the specified detail message.
     *
     * @param message the detailed error message explaining the cause
     */
    public Iso8583ParseException(String message) {
        super(message);
    }

    /**
     * Constructs a new Iso8583ParseException with the specified detail message and cause.
     *
     * @param message the detailed error message explaining the cause
     * @param cause   the underlying cause of this exception
     */
    public Iso8583ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
