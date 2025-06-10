package com.floss83.javaswitch.tokenization;

/**
 * TokenizationDemo
 * ----------------
 * Manual sanity check for the Floss83 tokenization + HSM sim flow.
 * No JUnit needed—just run from your IDE or terminal.
 * mvn compile exec:java
 * -Dexec.mainClass=com.floss83.javaswitch.tokenization.TokenizationDemo
 * <==========run this
 */
public class TokenizationDemo {
    public static void main(String[] args) {
        // 1. Setup fake HSM and tokenization service
        HsmSimulator hsm = new HsmSimulator();
        TokenizationService service = new TokenizationService(hsm);

        // 2. Demo PAN and CVV (test data, never real values)
        String pan = "5454545454545454";
        String cvv = "123";

        // 3. Tokenize
        String panToken = service.tokenizePan(pan);
        String cvvToken = service.tokenizeCvv(cvv);

        System.out.println("=== TOKENIZATION DEMO ===");
        System.out.println("Original PAN: " + pan);
        System.out.println("PAN Token:    " + panToken);
        System.out.println("Original CVV: " + cvv);
        System.out.println("CVV Token:    " + cvvToken);

        // 4. Detokenize (should match originals)
        String detokPan = service.detokenizePan(panToken);
        String detokCvv = service.detokenizeCvv(cvvToken);

        System.out.println("Detokenized PAN: " + detokPan);
        System.out.println("Detokenized CVV: " + detokCvv);

        // 5. Negative test (tampered token)
        try {
            System.out.println("Trying to detokenize tampered PAN token...");
            String tampered = panToken.substring(0, panToken.length() - 1) + "A";
            service.detokenizePan(tampered);
        } catch (Exception e) {
            System.out.println("Expected failure for tampered token: " + e.getMessage());
        }
    }
}
