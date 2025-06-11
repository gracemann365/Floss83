package com.floss83.javaswitch.iso8583;

import java.util.BitSet;

/**
 * Comprehensive ISO 8583 message parser for financial transactions.
 * <p>
 * Supports parsing of MTI, primary and secondary bitmaps, and
 * extraction of fixed length, LLVAR, and LLLVAR data elements.
 * </p>
 * <p>
 * Throws {@link Iso8583ParseException} on any message format violations
 * or malformed inputs.
 * </p>
 * 
 * Usage example:
 * 
 * <pre>
 * Iso8583Parser parser = new Iso8583Parser();
 * Iso8583Message message = parser.parse(rawIsoMessage);
 * </pre>
 * 
 * @author Gracemann365
 * @since 1.0
 */
public class Iso8583Parser {

    /**
     * Parses a raw ISO 8583 message string into an {@link Iso8583Message}.
     * 
     * @param message raw ISO 8583 message string (ASCII-encoded)
     * @return parsed Iso8583Message object
     * @throws Iso8583ParseException on parse failure
     */
    public Iso8583Message parse(String message) throws Iso8583ParseException {
        if (message == null || message.length() < 20) {
            throw new Iso8583ParseException("Input too short to contain MTI and primary bitmap.");
        }

        Iso8583Message isoMessage = new Iso8583Message();

        // MTI
        isoMessage.setMti(parseMti(message));

        // Primary bitmap
        BitSet primaryBitmap = parseBitmap(message, 4);
        isoMessage.setPrimaryBitmap(primaryBitmap);

        int cursor = 20;

        // Secondary bitmap
        BitSet secondaryBitmap = null;
        if (primaryBitmap.get(0)) {
            if (message.length() < cursor + 16) {
                throw new Iso8583ParseException("Secondary bitmap indicated but missing.");
            }
            secondaryBitmap = parseBitmap(message, cursor);
            isoMessage.setSecondaryBitmap(secondaryBitmap);
            cursor += 16;
        }

        // Combine bitmaps for field presence
        BitSet fieldsPresent = combineBitmaps(primaryBitmap, secondaryBitmap);

        // Extract data elements
        cursor = parseDataElements(message, cursor, fieldsPresent, isoMessage);

        return isoMessage;
    }

    private String parseMti(String message) {
        return message.substring(0, 4);
    }

    private BitSet parseBitmap(String message, int startIndex) throws Iso8583ParseException {
        String hex = message.substring(startIndex, startIndex + 16);
        return hexToBitSet(hex);
    }

    private BitSet hexToBitSet(String hex) throws Iso8583ParseException {
        if (hex == null || hex.length() != 16) {
            throw new Iso8583ParseException("Bitmap must be 16 hex characters");
        }
        BitSet bits = new BitSet(64);
        for (int i = 0; i < 16; i++) {
            int val = Character.digit(hex.charAt(i), 16);
            if (val == -1)
                throw new Iso8583ParseException("Invalid hex in bitmap: " + hex.charAt(i));
            for (int bit = 0; bit < 4; bit++) {
                if ((val & (1 << (3 - bit))) != 0)
                    bits.set(i * 4 + bit);
            }
        }
        return bits;
    }

    private BitSet combineBitmaps(BitSet primary, BitSet secondary) {
        BitSet combined = new BitSet(128);
        combined.or(primary);
        if (secondary != null) {
            for (int i = 0; i < 64; i++) {
                if (secondary.get(i))
                    combined.set(i + 64);
            }
        }
        return combined;
    }

    private int parseDataElements(String message, int cursor, BitSet fieldsPresent, Iso8583Message isoMessage)
            throws Iso8583ParseException {
        for (int fieldNum = 2; fieldNum <= 128; fieldNum++) {
            if (!fieldsPresent.get(fieldNum - 1))
                continue;

            Iso8583Field fieldDef = Iso8583Field.getByFieldNumber(fieldNum);
            if (fieldDef == null)
                throw new Iso8583ParseException("Unsupported field: " + fieldNum);

            int fieldLength;
            if (fieldDef.isVariableLength()) {
                int lengthDigits = (fieldDef.getMaxLength() > 99) ? 3 : 2;
                if (cursor + lengthDigits > message.length())
                    throw new Iso8583ParseException("Insufficient data for length of field " + fieldNum);

                String lengthStr = message.substring(cursor, cursor + lengthDigits);
                try {
                    fieldLength = Integer.parseInt(lengthStr);
                } catch (NumberFormatException e) {
                    throw new Iso8583ParseException("Invalid length format in field " + fieldNum);
                }
                cursor += lengthDigits;

                if (fieldLength > fieldDef.getMaxLength()) {
                    throw new Iso8583ParseException("Field " + fieldNum + " length " + fieldLength +
                            " exceeds max allowed " + fieldDef.getMaxLength());
                }
            } else {
                fieldLength = fieldDef.getMaxLength();
            }

            if (cursor + fieldLength > message.length())
                throw new Iso8583ParseException("Insufficient data for field " + fieldNum);

            String value = message.substring(cursor, cursor + fieldLength);
            if (!validateFieldFormat(fieldNum, value)) {
                throw new Iso8583ParseException("Invalid format in field " + fieldNum);
            }

            cursor += fieldLength;
            isoMessage.setDataElement(fieldNum, value);
        }
        return cursor;
    }

    /**
     * Basic validation for common field formats.
     * Extend as needed with regex or more complex rules.
     * 
     * @param fieldNum ISO field number
     * @param value    Field value string
     * @return true if format is valid, false otherwise
     */
    private boolean validateFieldFormat(int fieldNum, String value) {
        // Example: check numeric fields for digits only
        return switch (fieldNum) {
            case 2, 3, 4, 11 -> value.matches("\\d+");
            default -> true;
        }; // PAN numeric
        // Processing code numeric
        // Amount numeric
        // System trace audit number numeric
        // Accept any format by default
    }
}
