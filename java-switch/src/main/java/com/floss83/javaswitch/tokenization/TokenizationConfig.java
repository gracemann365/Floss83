package com.floss83.javaswitch.tokenization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ===============================
 * TokenizationConfig
 * ===============================
 * <p>
 * Spring @Configuration class that provides the core PCI-style security bean
 * wiring
 * for the Floss83 card switch engine.
 * </p>
 *
 * <h3>What it does:</h3>
 * <ul>
 * <li>
 * Defines a singleton {@link TokenizationService} bean, ensuring that all
 * sensitive card data
 * tokenization is performed via a secure, auditable, and centrally managed
 * service.
 * </li>
 * <li>
 * Injects a new {@link HsmSimulator} instance (simulated HSM, ephemeral
 * in-memory keys)
 * into the TokenizationService constructor, aligning with PCI-DSS
 * separation-of-duties principles.
 * </li>
 * <li>
 * Ensures the HSM is initialized before use, establishing a deterministic but
 * non-persistent
 * crypto key for all tokenization/detokenization flows in dev/test.
 * </li>
 * </ul>
 *
 * <h3>Why this matters:</h3>
 * <ul>
 * <li>
 * All controller/service logic using TokenizationService will operate via
 * secure tokens, never raw PAN/CVV.
 * </li>
 * <li>
 * Audit and security reviewers can see at a glance that key management and
 * crypto flows are not scattered,
 * but handled by explicit, testable, and easily swappable bean definitions.
 * </li>
 * <li>
 * Upgrading to a real HSM (hardware or network) later is a single config
 * change.
 * </li>
 * </ul>
 *
 * <h3>PCI-Grade Example:</h3>
 * 
 * <pre>
 * @Autowired
 * private TokenizationService tokenizationService;
 * // ... tokenizationService.tokenizePan(rawPan);
 * </pre>
 *
 * <h3>Security Note:</h3>
 * <ul>
 * <li>
 * This config is for DEV/TEST. For production, inject a real HSM or KMS
 * provider and never use ephemeral keys.
 * </li>
 * </ul>
 *
 * @author Floss83
 * @version 1.0
 */
@Configuration
public class TokenizationConfig {

    /**
     * Provides a singleton TokenizationService with an in-memory HSM simulator for
     * dev/test use.
     * All crypto keys are ephemeral and should be replaced by production-grade key
     * management before live use.
     *
     * @return TokenizationService configured with HsmSimulator
     */
    @Bean
    public TokenizationService tokenizationService() {
        HsmSimulator hsm = new HsmSimulator();
        hsm.initializeKeys(); // Optional: for deterministic dev runs
        return new TokenizationService(hsm);
    }
}
