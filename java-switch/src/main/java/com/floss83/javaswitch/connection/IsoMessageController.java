package com.floss83.javaswitch.connection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floss83.javaswitch.iso8583.Iso8583Message;
import com.floss83.javaswitch.iso8583.Iso8583ParseException;
import com.floss83.javaswitch.iso8583.Iso8583Parser;

/**
 * IsoMessageController exposes a REST API endpoint to receive ISO 8583 messages
 * over HTTP. Accepts raw ISO 8583 strings via POST and prints them to the console.
 *
 * <p>
 * Example usage (HTTP POST):
 * <pre>
 *     POST /api/iso8583
 *     Body: <ISO 8583 message string>
 * </pre>
 * </p>
 *
 * <p>
 * Designed for manual/local testing with tools such as curl or Postman.
 * </p>
 *
 * @author Floss83
 * @version 1.0
 */
/**
 * IsoMessageController receives ISO 8583 messages via HTTP and parses them.
 */
@RestController
@RequestMapping("/api/iso8583")
public class IsoMessageController {

    private final Iso8583Parser parser = new Iso8583Parser();

    /**
     * Accepts POSTed ISO 8583 raw messages, parses and prints results.
     *
     * @param isoMessage Raw ISO 8583 string
     * @return 200 OK with parsed info, or 400 Bad Request on parse error
     */
    @PostMapping
    public ResponseEntity<String> receiveIsoMessage(@RequestBody String isoMessage) {
        System.out.println("[HTTP] Received: " + isoMessage);
        try {
            Iso8583Message parsed = parser.parse(isoMessage);
            StringBuilder sb = new StringBuilder();
            sb.append("MTI: ").append(parsed.getMti()).append("\n");
            parsed.getDataElements().forEach((field, value) -> {
                sb.append("Field ").append(field).append(": ").append(value).append("\n");
            });
            System.out.println("[HTTP] Parsed:\n" + sb);
            return ResponseEntity.ok("Parsed OK:\n" + sb);
        } catch (Iso8583ParseException ex) {
            System.err.println("[HTTP] Parse Error: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Parse error: " + ex.getMessage());
        }
    }
}