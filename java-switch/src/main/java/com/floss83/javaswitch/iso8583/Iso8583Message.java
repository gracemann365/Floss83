package com.floss83.javaswitch.iso8583;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a fully parsed ISO 8583 financial message.
 * <p>
 * This class encapsulates the core components of an ISO 8583 message:
 * <ul>
 *   <li>Message Type Indicator (MTI)</li>
 *   <li>Primary and optional Secondary Bitmaps</li>
 *   <li>Data Elements mapped by field number</li>
 * </ul>
 * <p>
 * Instances of this class are immutable post parsing to ensure message integrity for audit and compliance.
 * All accessors provide read-only views or copies where necessary.
 * </p>
 * <p><b>Usage:</b> Created and populated by {@link Iso8583Parser} during message decoding.</p>
 *
 * @author Gracemann365
 * @since 1.0
 */
public class Iso8583Message {

    /** ISO 8583 Message Type Indicator (4-digit numeric string). */
    private String mti;

    /** Primary bitmap representing presence of data elements 1-64. */
    private BitSet primaryBitmap;

    /** Secondary bitmap representing presence of data elements 65-128; may be null if absent. */
    private BitSet secondaryBitmap;

    /** Map storing parsed data elements: field number → value. */
    private final Map<Integer, String> dataElements;

    /**
     * Constructs an empty Iso8583Message instance.
     * Initializes internal data structures.
     */
    public Iso8583Message() {
        this.dataElements = new HashMap<>();
    }

    /**
     * Returns the Message Type Indicator (MTI).
     *
     * @return the 4-character MTI string
     */
    public String getMti() {
        return mti;
    }

    /**
     * Sets the Message Type Indicator (MTI).
     * Package-private to restrict modification after parsing.
     *
     * @param mti the 4-character MTI string
     */
    void setMti(String mti) {
        this.mti = mti;
    }

    /**
     * Returns the primary bitmap indicating present data elements (1-64).
     *
     * @return the primary bitmap as a BitSet
     */
    public BitSet getPrimaryBitmap() {
        return primaryBitmap;
    }

    /**
     * Sets the primary bitmap.
     * Package-private to restrict modification after parsing.
     *
     * @param primaryBitmap BitSet representing presence of fields 1-64
     */
    void setPrimaryBitmap(BitSet primaryBitmap) {
        this.primaryBitmap = primaryBitmap;
    }

    /**
     * Returns the secondary bitmap indicating present data elements (65-128).
     *
     * @return the secondary bitmap as a BitSet, or null if absent
     */
    public BitSet getSecondaryBitmap() {
        return secondaryBitmap;
    }

    /**
     * Sets the secondary bitmap.
     * Package-private to restrict modification after parsing.
     *
     * @param secondaryBitmap BitSet representing presence of fields 65-128
     */
    void setSecondaryBitmap(BitSet secondaryBitmap) {
        this.secondaryBitmap = secondaryBitmap;
    }

    /**
     * Returns an unmodifiable view of the data elements map.
     *
     * @return map of field number to field value string
     */
    public Map<Integer, String> getDataElements() {
        return Map.copyOf(dataElements);
    }

    /**
     * Associates a field number with its parsed value.
     * Package-private to restrict external modification.
     *
     * @param fieldNumber the ISO 8583 data element number (1-128)
     * @param value the string value of the data element
     */
    void setDataElement(int fieldNumber, String value) {
        dataElements.put(fieldNumber, value);
    }

    /**
     * Retrieves the value for the specified data element number.
     *
     * @param fieldNumber the data element number
     * @return the string value of the data element, or null if not present
     */
    public String getDataElement(int fieldNumber) {
        return dataElements.get(fieldNumber);
    }
}
