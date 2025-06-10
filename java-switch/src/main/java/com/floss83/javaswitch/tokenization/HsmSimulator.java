package com.floss83.javaswitch.tokenization;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * HsmSimulator
 * ================================
 * 
 * This class simulates a Hardware Security Module (HSM) for local development
 * and testing.
 * 
 * <b>What is a real HSM?</b>
 * -------------------------
 * A <b>Hardware Security Module (HSM)</b> is a physical, tamper-resistant
 * computing device that
 * securely manages, stores, and processes cryptographic keys, digital
 * signatures, and secrets.
 * 
 * In real-world banking and payments systems:
 * - HSMs are PCI/DSS-certified appliances (often rack-mounted hardware).
 * - They handle ALL sensitive cryptographic operations (e.g., PAN/CVV
 * encryption, PIN verification, digital signatures).
 * - Keys never leave the HSM in plaintext.
 * - All access and operations are strictly audited, and physical tampering
 * attempts destroy the secrets inside.
 * - They are often required for regulatory compliance (PCI DSS, FIPS 140-2,
 * etc).
 * 
 * <b>Why simulate in Java?</b>
 * --------------------------
 * For open-source projects and local development, real HSM hardware is
 * impractical/expensive.
 * This class uses Java crypto APIs to mimic HSM functions:
 * - <b>In-memory keys</b> (never written to disk)
 * - Deterministic for testability (NEVER do this in prod)
 * - <b>NOT SECURE for production use!</b>
 * 
 * <b>In production:</b> Replace this with real HSM integration via PKCS#11,
 * JCE, or network HSM APIs.
 * 
 * <b>NOTE:</b> Never expose raw keys or full sensitive data in logs, commits,
 * or documentation!
 * 
 * -----
 * For security reviews: this is a simulation layer only; all sensitive flows
 * must migrate to real HSM-backed crypto before live/PCI usage.
 */

public class HsmSimulator {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 128; // 128-bit for demo

    private SecretKey secretKey;
    private byte[] iv; // Initialization Vector

    /**
     * Initializes deterministic AES key and IV for local development/testing.
     */
    public void initializeKeys() {
        try {
            // Deterministic key generation for repeatable tests
            byte[] seed = "Floss83-Tokenization-Demo-Seed".getBytes();
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed);
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE, secureRandom);
            this.secretKey = keyGen.generateKey();

            // Deterministic IV as well
            this.iv = new byte[16];
            secureRandom.nextBytes(this.iv);

            System.out.println("[AUDIT] HSM keys initialized (deterministic, dev only).");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize HSM keys", e);
        }
    }

    /**
     * Encrypts plaintext string with AES/CBC.
     * 
     * @param plaintext Raw string to encrypt (e.g., PAN, CVV)
     * @return Base64-encoded ciphertext
     */
    public String encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encrypted = cipher.doFinal(plaintext.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("HSM encryption failed", e);
        }
    }

    /**
     * Decrypts Base64-encoded ciphertext back to plaintext.
     * 
     * @param encryptedData Base64 string
     * @return Decrypted plaintext string
     */
    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("HSM decryption failed", e);
        }
    }
}
