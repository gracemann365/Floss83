package com.floss83.javaswitch.tokenization;

import java.time.Instant;

/**
 * TokenizationService
 * =========================
 *
 * Handles secure, auditable tokenization (encryption) and detokenization
 * (decryption) of sensitive card data
 * such as PAN (Primary Account Number) and CVV (Card Verification Value), via
 * an in-memory simulated HSM.
 *
 * <b>Real-world mapping:</b>
 * - In a true production-grade card switch, NO downstream system should ever
 * see real PAN/CVV outside of a secure enclave (i.e., an HSM).
 * - Tokenization here means: take the raw value (e.g., 16-digit PAN), encrypt
 * it using strong AES, and store only the encrypted string (the token).
 * - Detokenization means: take the encrypted token, decrypt it inside the
 * vault, and only expose the clear value for permitted, auditable flows (e.g.,
 * network processing).
 *
 * <b>Security discipline:</b>
 * - All logging redacts/masks sensitive data, exposing at most last 4 digits
 * (PCI-DSS practice).
 * - All operations are logged with timestamp and status for audit trails.
 * - This is a DEV/TEST implementation: keys are ephemeral and in-memory, NOT
 * for prod!
 *
 * <b>When plugging in a real HSM:</b>
 * - You would replace this service’s HsmSimulator with an HSM provider
 * implementation using PKCS#11/JCE or network HSM APIs.
 * - All code using this service will require NO changes (open/closed
 * principle).
 *
 * <b>Core Domain Flows:</b>
 * - Card Acquiring: PAN/CVV from merchant or POS terminal is immediately
 * tokenized before further handling.
 * - Card Issuing: Tokenized card details are safely routed and only detokenized
 * when strictly required (e.g., outgoing ISO 8583 message, PIN verification).
 */
public class TokenizationService {

    /**
     * Simulated HSM (Hardware Security Module) – handles encryption/decryption.
     * In prod, replace with real HSM integration.
     */
    private final HsmSimulator hsm;

    /**
     * Initializes the tokenization service with an HSM (simulator for dev).
     * 
     * @param hsm Instance of HsmSimulator or HSM integration.
     */
    public TokenizationService(HsmSimulator hsm) {
        this.hsm = hsm;
        this.hsm.initializeKeys();
    }

    /**
     * Tokenizes (encrypts) a PAN (card number) using the simulated HSM.
     * 
     * @param pan Raw card PAN (should be 13–19 digits).
     * @return Token (encrypted string); NEVER the real PAN.
     */
    public String tokenizePan(String pan) {
        validatePan(pan);
        String token = hsm.encrypt(pan);
        auditLog("TOKENIZE_PAN", pan, token, "SUCCESS");
        return token;
    }

    /**
     * Detokenizes (decrypts) a token back to the original PAN.
     * 
     * @param token Encrypted token value.
     * @return Decrypted PAN if valid.
     */
    public String detokenizePan(String token) {
        try {
            String pan = hsm.decrypt(token);
            auditLog("DETOKENIZE_PAN", token, pan, "SUCCESS");
            return pan;
        } catch (Exception e) {
            auditLog("DETOKENIZE_PAN", token, null, "FAIL: " + e.getMessage());
            throw new IllegalArgumentException("PAN detokenization failed.", e);
        }
    }

    /**
     * Tokenizes (encrypts) a CVV using the simulated HSM.
     * 
     * @param cvv Raw CVV (usually 3 or 4 digits).
     * @return Token (encrypted string).
     */
    public String tokenizeCvv(String cvv) {
        validateCvv(cvv);
        String token = hsm.encrypt(cvv);
        auditLog("TOKENIZE_CVV", cvv, token, "SUCCESS");
        return token;
    }

    /**
     * Detokenizes (decrypts) a token back to the original CVV.
     * 
     * @param token Encrypted token value.
     * @return Decrypted CVV if valid.
     */
    public String detokenizeCvv(String token) {
        try {
            String cvv = hsm.decrypt(token);
            auditLog("DETOKENIZE_CVV", token, cvv, "SUCCESS");
            return cvv;
        } catch (Exception e) {
            auditLog("DETOKENIZE_CVV", token, null, "FAIL: " + e.getMessage());
            throw new IllegalArgumentException("CVV detokenization failed.", e);
        }
    }

    /**
     * Logs every tokenization/detokenization operation for audit.
     * Never exposes full PAN/CVV—only last 4 digits or masked.
     */
    private void auditLog(String event, String input, String output, String status) {
        System.out.printf(
                "[AUDIT] %-18s | %s | IN: %s | OUT: %s | %s%n",
                event,
                Instant.now(),
                mask(input),
                mask(output),
                status);
    }

    /**
     * Masks input for logging: shows only last 4 characters for PAN/CVV.
     * If the value is a token (usually much longer), shows as "***".
     */
    public String mask(String val) {
        if (val == null)
            return null;
        if (val.matches("\\d{6,19}")) // Looks like a PAN
            return "***" + val.substring(val.length() - 4);
        if (val.matches("\\d{3,4}")) // Looks like a CVV
            return "***" + val.substring(val.length() - 1);
        if (val.length() > 10) // Probably a token
            return "***";
        return val;
    }

    /**
     * Domain validation: PAN must be 13–19 digits (ISO 7812).
     */
    private void validatePan(String pan) {
        if (pan == null || !pan.matches("\\d{13,19}")) {
            throw new IllegalArgumentException("Invalid PAN: Must be 13-19 digits.");
        }
    }

    /**
     * Domain validation: CVV must be 3–4 digits.
     */
    private void validateCvv(String cvv) {
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            throw new IllegalArgumentException("Invalid CVV: Must be 3 or 4 digits.");
        }
    }
}
