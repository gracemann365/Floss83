/**
 * ===============================
 * Floss83 PCI-Grade Controller Flow
 * ===============================
 *
 * 1. Why Tokenize at Controller Level?
 *    - PCI-DSS: Protect all sensitive card data (PAN/CVV) as early as possible—immediately on backend entry.
 *    - Controller = trust boundary. Tokenize/validate as soon as ISO 8583 arrives, so nothing downstream ever sees real card data.
 *    - Everything after this class works only with tokens (never raw secrets), minimizing audit/compliance risk.
 *
 * 2. How Tokenization Works Here:
 *    - Controller parses incoming ISO 8583, extracts Field 2 (PAN) and Field 52 (CVV, if present).
 *    - Calls TokenizationService to tokenize PAN and CVV.
 *    - Audit logs are written for every operation, PCI-style masking enforced.
 *    - Downstream logic gets only tokens, not secrets.
 *
 * 3. Real-World Precedent:
 *    - This pattern matches Visa/Mastercard/Stripe: tokenize at the “door,” not deep in the stack.
 *    - Auditors and CTOs want to see this “cut-off” up front.
 *
 * 4. Diagram:
 *
 *        +--------HTTP POST--------+
 *        | ISO 8583 message (raw)  |
 *        +-----------+-------------+
 *                    |
 *                    v
 *        +-----------+-------------+
 *        | IsoMessageController    |
 *        | - parses ISO 8583       |
 *        | - tokenizes PAN, CVV    |
 *        | - logs audit (masked)   |
 *        +-----------+-------------+
 *                    |
 *         tokens only v
 *        +-----------+-------------+
 *        | Downstream/Other Layers |
 *        +------------------------+
 *
 * 5. Bottom Line:
 *    - PCI-safe, audit-grade, clear separation of concerns.
 *    - No “oops” leaks in logs or future services.
 */
package com.floss83.javaswitch.connection;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floss83.javaswitch.iso8583.Iso8583Message;
import com.floss83.javaswitch.iso8583.Iso8583ParseException;
import com.floss83.javaswitch.iso8583.Iso8583Parser;
import com.floss83.javaswitch.tokenization.TokenizationService;

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
    private final TokenizationService tokenizationService;

    /**
     * Spring will inject the TokenizationService bean.
     */
    public IsoMessageController(TokenizationService tokenizationService) {
        this.tokenizationService = tokenizationService;
    }

    /**
     * Accepts POSTed ISO 8583 raw messages, parses, tokenizes PAN/CVV, prints
     * results.
     *
     * @param isoMessage Raw ISO 8583 string
     * @return 200 OK with parsed info (tokens), or 400 Bad Request on parse error
     */
    @PostMapping
    public ResponseEntity<String> receiveIsoMessage(@RequestBody String isoMessage) {
        System.out.println("[HTTP] Received: " + maskForLog(isoMessage));
        try {
            Iso8583Message parsed = parser.parse(isoMessage);

            StringBuilder sb = new StringBuilder();
            sb.append("MTI: ").append(parsed.getMti()).append("\n");

            Map<Integer, String> fields = parsed.getMutableDataElements();

            // Tokenize PAN (Field 2) and CVV (Field 52) if present
            String pan = fields.get(2); // PAN
            String cvv = fields.get(52); // CVV (optional)
            String panToken = null;
            String cvvToken = null;

            if (pan != null) {
                panToken = tokenizationService.tokenizePan(pan);
                sb.append("Field 2 (PAN, tokenized): ").append(maskForLog(panToken)).append("\n");
            }
            if (cvv != null) {
                cvvToken = tokenizationService.tokenizeCvv(cvv);
                sb.append("Field 52 (CVV, tokenized): ").append(maskForLog(cvvToken)).append("\n");
            }

            // Print all fields, masking sensitive values
            fields.forEach((field, value) -> {
                if (field == 2 || field == 52)
                    return; // already handled
                sb.append("Field ").append(field).append(": ").append(maskForLog(value)).append("\n");
            });

            System.out.println("[HTTP] Parsed (with tokens):\n" + sb);
            return ResponseEntity.ok("Parsed OK (tokens):\n" + sb);

        } catch (Iso8583ParseException ex) {
            System.err.println("[HTTP] Parse Error: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Parse error: " + ex.getMessage());
        }
    }

    /**
     * Masks PAN, CVV, or tokens for logging; last 4 for PAN, otherwise all masked.
     * 
     * @param value field value
     * @return masked string for logs
     */
    private String maskForLog(String value) {
        if (value == null)
            return null;
        if (value.matches("\\d{13,19}")) // PAN
            return "***" + value.substring(value.length() - 4);
        if (value.matches("\\d{3,4}")) // CVV
            return "***";
        if (value.length() > 10) // probably a token
            return "***";
        return value;
    }
}
