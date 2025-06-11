package com.floss83.javaswitch.iso8583;
/**
 * <h2>ISO 8583 Field Enumeration (Enterprise Edition)</h2>
 * <p>
 * Complete, audit-focused enumeration of ISO 8583:1987 data elements.
 * <ul>
 *   <li>Field number (2–64, extendable to 128)</li>
 *   <li>ISO description</li>
 *   <li>Max length, variable/fixed, data type</li>
 *   <li>Notes on PCI-sensitivity, audit</li>
 * </ul>
 * <p>
 * Used for message parsing, UI builders, validation, and documentation.
 * </p>
 * <p>
 * <b>Author:</b> David Grace <br>
 * <b>Since:</b> 1.0
 * </p>
 */

/**
 * ISO 8583:1987 Data Elements – Full Enterprise Enumeration
 * Field number, ISO name, length, variable/fixed, type, doc.
 * PCI-sensitive, audit-focused. Extend as needed for fields 65–128.
 */
public enum Iso8583Field {

    // PAN/Cardholder Data
    PAN(2, "Primary Account Number (PAN)", 19, true, "n",
            "LLVAR. Cardholder account number (PCI-sensitive, mask for audit)."),
    PROCESSING_CODE(3, "Processing Code", 6, false, "n", "Transaction type/subtype. Fixed 6n."),
    TRANSACTION_AMOUNT(4, "Amount, Transaction", 12, false, "n", "Amount authorized in minor units. Fixed 12n."),
    SETTLEMENT_AMOUNT(5, "Amount, Settlement", 12, false, "n", "Amount for settlement. Fixed 12n."),
    CARDHOLDER_BILLING_AMOUNT(6, "Amount, Cardholder Billing", 12, false, "n",
            "Amount in cardholder’s currency. Fixed 12n."),
    TRANSMISSION_DATE_TIME(7, "Transmission Date & Time", 10, false, "n", "MMDDhhmmss, GMT. Fixed 10n."),
    SETTLEMENT_CONVERSION_RATE(9, "Conversion Rate, Settlement", 8, false, "n",
            "Rate applied for settlement. Fixed 8n."),
    BILLING_CONVERSION_RATE(10, "Conversion Rate, Cardholder Billing", 8, false, "n",
            "Rate applied for billing. Fixed 8n."),
    SYSTEM_TRACE_AUDIT_NUMBER(11, "System Trace Audit Number (STAN)", 6, false, "n",
            "Unique per terminal, per day. Fixed 6n."),
    LOCAL_TRANSACTION_TIME(12, "Local Transaction Time", 6, false, "n", "hhmmss, terminal time. Fixed 6n."),
    LOCAL_TRANSACTION_DATE(13, "Local Transaction Date", 4, false, "n", "MMDD, terminal date. Fixed 4n."),
    EXPIRATION_DATE(14, "Expiration Date", 4, false, "n", "YYMM, card expiry. Fixed 4n."),
    SETTLEMENT_DATE(15, "Settlement Date", 4, false, "n", "MMDD. Fixed 4n."),
    CONVERSION_DATE(16, "Conversion Date", 4, false, "n", "MMDD. Fixed 4n."),
    CAPTURE_DATE(17, "Capture Date", 4, false, "n", "MMDD. Fixed 4n."),
    MERCHANT_TYPE(18, "Merchant Type", 4, false, "n", "MCC (Merchant Category Code). Fixed 4n."),
    ACQUIRING_INSTITUTION_COUNTRY_CODE(19, "Acquiring Institution Country Code", 3, false, "n",
            "ISO country code. Fixed 3n."),
    PAN_EXTENDED_COUNTRY_CODE(20, "PAN Extended Country Code", 3, false, "n", "ISO country code. Fixed 3n."),
    FORWARDING_INSTITUTION_COUNTRY_CODE(21, "Forwarding Institution Country Code", 3, false, "n",
            "ISO country code. Fixed 3n."),
    POINT_OF_SERVICE_ENTRY_MODE(22, "Point of Service Entry Mode", 3, false, "n", "POS entry. Fixed 3n."),
    CARD_SEQUENCE_NUMBER(23, "Card Sequence Number", 3, false, "n", "2–3 digits for reissued cards. Fixed 3n."),
    FUNCTION_CODE(24, "Function Code (Network International ID)", 4, false, "n", "Message function. Fixed 4n."),
    POINT_OF_SERVICE_CONDITION_CODE(25, "Point of Service Condition Code", 2, false, "n",
            "Transaction conditions. Fixed 2n."),
    POINT_OF_SERVICE_CAPTURE_CODE(26, "Point of Service Capture Code", 2, false, "n", "Capture method. Fixed 2n."),
    AUTH_ID_RESPONSE_LENGTH(27, "Authorizing ID Response Length", 1, false, "n", "Length of auth ID. Fixed 1n."),
    AMOUNT_TRANSACTION_FEE(28, "Amount, Transaction Fee", 9, false, "xn", "Fee, signed. Fixed 9xn."),
    AMOUNT_SETTLEMENT_FEE(29, "Amount, Settlement Fee", 9, false, "xn", "Fee, signed. Fixed 9xn."),
    AMOUNT_TRANSACTION_PROCESSING_FEE(30, "Amount, Transaction Processing Fee", 9, false, "xn",
            "Processing fee. Fixed 9xn."),
    AMOUNT_SETTLEMENT_PROCESSING_FEE(31, "Amount, Settlement Processing Fee", 9, false, "xn",
            "Processing fee. Fixed 9xn."),
    ACQUIRING_INSTITUTION_ID(32, "Acquiring Institution ID Code", 11, true, "n",
            "LLVAR. Numeric/acquirer. Up to 11 digits."),
    FORWARDING_INSTITUTION_ID(33, "Forwarding Institution ID Code", 11, true, "n",
            "LLVAR. Numeric/forwarder. Up to 11 digits."),
    PAN_EXTENDED(34, "PAN Extended", 28, true, "z", "LLVAR. Track 2 extended data."),
    TRACK_2_DATA(35, "Track 2 Data", 37, true, "z", "LLVAR. Card magstripe data, PCI-sensitive."),
    TRACK_3_DATA(36, "Track 3 Data", 104, true, "z", "LLVAR. ISO magstripe, rarely used, PCI-sensitive."),
    RETRIEVAL_REFERENCE_NUMBER(37, "Retrieval Reference Number", 12, false, "an", "Fixed 12an. Unique ref (RRN)."),
    AUTHORIZATION_ID_RESPONSE(38, "Authorization ID Response", 6, false, "an", "Fixed 6an. Approval code."),
    RESPONSE_CODE(39, "Response Code", 2, false, "an", "Fixed 2an. E.g. 00=Approved."),
    SERVICE_RESTRICTION_CODE(40, "Service Restriction Code", 3, false, "n", "Fixed 3n. Network usage."),
    CARD_ACCEPTOR_TERMINAL_ID(41, "Card Acceptor Terminal ID", 8, false, "ans", "Fixed 8ans. POS terminal ID."),
    CARD_ACCEPTOR_ID_CODE(42, "Card Acceptor ID Code", 15, false, "ans", "Fixed 15ans. Merchant/ATM owner ID."),
    CARD_ACCEPTOR_NAME_LOCATION(43, "Card Acceptor Name/Location", 40, false, "ans",
            "Fixed 40ans. Merchant name, city, country."),
    ADDITIONAL_RESPONSE_DATA(44, "Additional Response Data", 25, true, "an", "LLVAR. ISO/processor-specific."),
    TRACK_1_DATA(45, "Track 1 Data", 76, true, "ans", "LLVAR. Magstripe Track 1 data."),
    ADDITIONAL_DATA(46, "Additional Data – ISO", 999, true, "an", "LLLVAR. ISO reserved."),
    ADDITIONAL_DATA_NATIONAL(47, "Additional Data – National", 999, true, "an", "LLLVAR. Country/region reserved."),
    ADDITIONAL_DATA_PRIVATE(48, "Additional Data – Private", 999, true, "an", "LLLVAR. Private/processor fields."),
    CURRENCY_CODE_TRANSACTION(49, "Currency Code, Transaction", 3, false, "n", "Fixed 3n. ISO currency code."),
    CURRENCY_CODE_SETTLEMENT(50, "Currency Code, Settlement", 3, false, "n", "Fixed 3n. ISO currency code."),
    CURRENCY_CODE_CARDHOLDER_BILLING(51, "Currency Code, Cardholder Billing", 3, false, "n",
            "Fixed 3n. ISO currency code."),
    PERSONAL_ID_NUMBER_DATA(52, "Personal ID Number Data", 16, false, "b",
            "Fixed 16b. Encrypted PIN block, PCI-sensitive."),
    SECURITY_RELATED_CONTROL_INFO(53, "Security Related Control Information", 48, false, "an",
            "Fixed 48an. Security features."),
    ADDITIONAL_AMOUNTS(54, "Additional Amounts", 120, true, "an", "LLLVAR. Surcharges/fees."),
    RESERVED_ISO(55, "Reserved ISO", 255, true, "b", "LLLVAR. EMV data, chip auth."),
    RESERVED_NATIONAL(56, "Reserved National", 255, true, "an", "LLLVAR. National spec."),
    RESERVED_PRIVATE(57, "Reserved Private", 255, true, "an", "LLLVAR. Private/processor."),
    AUTHENTICATION_CODE(58, "Authentication Code", 6, false, "an", "Fixed 6an. Additional security."),
    RESPONSE_INDICATOR(59, "Response Indicator", 999, true, "an", "LLLVAR. Response flag."),
    PAYMENT_INFORMATION(60, "Payment Information", 999, true, "an", "LLLVAR. Installment/payment plan."),
    RESERVED_FOR_POS(61, "Reserved for POS", 999, true, "an", "LLLVAR. POS/terminal info."),
    RESERVED_FOR_NETWORK(62, "Reserved for Network", 999, true, "an", "LLLVAR. Network use."),
    RESERVED_FOR_ISSUER(63, "Reserved for Issuer", 999, true, "an", "LLLVAR. Issuer private use."),
    MAC(64, "Message Authentication Code (MAC)", 16, false, "b",
            "Fixed 16b. HMAC or MAC block, for message integrity."),

    // -- Extend below for fields 65–128 as needed for your use-case
    ;

    private final int fieldNumber;
    private final String description;
    private final int maxLength;
    private final boolean variableLength;
    private final String dataType;
    private final String notes;

    Iso8583Field(int fieldNumber, String description, int maxLength, boolean variableLength, String dataType,
            String notes) {
        this.fieldNumber = fieldNumber;
        this.description = description;
        this.maxLength = maxLength;
        this.variableLength = variableLength;
        this.dataType = dataType;
        this.notes = notes;
    }

    /**
     * @return The ISO 8583 field number (2–128).
     */
    public int getFieldNumber() {
        return fieldNumber;
    }

    /**
     * @return Official ISO 8583 description for this field.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Maximum allowed length for the field.
     *         For variable-length (LLVAR/LLLVAR), this is the maximum.
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * @return True if this field is variable-length (LLVAR/LLLVAR).
     */
    public boolean isVariableLength() {
        return variableLength;
    }

    /**
     * @return The ISO data type (n, an, ans, b, z, etc.)
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @return Notes and audit/security context.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Looks up the Iso8583Field by field number.
     * 
     * @param fieldNumber ISO 8583 data element number (2–128)
     * @return Corresponding Iso8583Field, or null if not found
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
