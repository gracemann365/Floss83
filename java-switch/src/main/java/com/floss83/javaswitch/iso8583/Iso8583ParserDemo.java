package com.floss83.javaswitch.iso8583;

/**
 * Quick demo to test Iso8583Parser with a sample ISO 8583 message.
 */
public class Iso8583ParserDemo {
    public static void main(String[] args) {
        // This is exactly 82 chars. It covers:
        // - MTI (4 chars) "0200"
        // - Primary bitmap (16 hex chars) "7238000000000000"
        // - Field 2 (LLVAR = "16" + "1234567890123456") = 18 chars
        // - Field 3 (6 chars) "000000"
        // - Field 4 (12 chars) "000000010000"
        // - Field 7 (10 chars) "0709163030"
        // - Field 11 (6 chars) "123456"
        // - Field 12 (6 chars) "163030"
        // - Field 13 (4 chars) "0709"
        String sampleMessage = "0200" // MTI
                + "7238000000000000" // primary bitmap (fields 2,3,4,7,11,12,13)
                + "16" // LLVAR length prefix for field 2 (PAN)
                + "1234567890123456" // PAN (16 chars)
                + "000000" // field 3
                + "000000010000" // field 4
                + "0709163030" // field 7
                + "123456" // field 11
                + "163030" // field 12
                + "0709"; // field 13

        // Confirm it’s 82 characters:
        System.out.println("Sample message length: " + sampleMessage.length());

        Iso8583Parser parser = new Iso8583Parser();
        try {
            Iso8583Message isoMessage = parser.parse(sampleMessage);

            System.out.println("MTI: " + isoMessage.getMti());
            System.out.println("Primary Bitmap: " + isoMessage.getPrimaryBitmap());
            System.out.println("Data Elements:");
            isoMessage.getDataElements().forEach((field, value) -> {
                System.out.println("Field " + field + ": " + value);
            });

        } catch (Iso8583ParseException e) {
            System.err.println("Parsing failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
