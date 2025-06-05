package com.floss83.javaswitch.iso8583;

/**
 * Enumeration representing standard ISO 8583 data elements.
 * <p>
 * Each enum constant defines:
 * <ul>
 * <li>Field number as per ISO 8583 specification</li>
 * <li>Human-readable description</li>
 * <li>Maximum length allowed for the field (for LLVAR, this is the max)</li>
 * <li>Whether the field is of variable length (LLVAR/LLLVAR)</li>
 * </ul>
 * </p>
 * <p>
 * This metadata supports the parser in correctly interpreting message fields,
 * enforcing length constraints, and handling variable-length data.
 * </p>
 *
 * @author Gracemann365
 * @since 1.0
 */
public enum Iso8583Field {

    PAN(2, "Primary Account Number", 19, true),
    PROCESSING_CODE(3, "Processing Code", 6, false),
    TRANSACTION_AMOUNT(4, "Transaction Amount", 12, false),
    TRANSMISSION_DATE_TIME(7, "Transmission Date & Time", 10, false),
    SYSTEM_TRACE_AUDIT_NUMBER(11, "System Trace Audit Number", 6, false),
    LOCAL_TRANSACTION_TIME(12, "Local Transaction Time", 6, false),
    LOCAL_TRANSACTION_DATE(13, "Local Transaction Date", 4, false),
    // You can add more fields here later, e.g.:
    // SETTLEMENT_DATE(15, "Settlement Date", 4, false),
    // ... etc.

    ;

    private final int fieldNumber;
    private final String description;
    private final int maxLength;
    private final boolean variableLength;

    Iso8583Field(int fieldNumber, String description, int maxLength, boolean variableLength) {
        this.fieldNumber = fieldNumber;
        this.description = description;
        this.maxLength = maxLength;
        this.variableLength = variableLength;
    }

    /**
     * Returns the ISO 8583 data element number.
     *
     * @return field number (1–128)
     */
    public int getFieldNumber() {
        return fieldNumber;
    }

    /**
     * Returns a human-readable description of the field.
     *
     * @return description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the maximum allowed length of this field.
     * For variable-length fields, this is the max allowed length.
     *
     * @return maximum field length in characters
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Indicates if this field is variable length (e.g., LLVAR or LLLVAR).
     *
     * @return true if variable length, false if fixed length
     */
    public boolean isVariableLength() {
        return variableLength;
    }

    /**
     * Looks up the Iso8583Field by field number.
     *
     * @param fieldNumber the ISO 8583 field number
     * @return corresponding Iso8583Field or null if not defined
     */
    public static Iso8583Field getByFieldNumber(int fieldNumber) {
        for (Iso8583Field field : values()) {
            if (field.fieldNumber == fieldNumber) {
                return field;
            }
        }
        return null;
    }
}
