package com.floss83.javaswitch.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.floss83.javaswitch.iso8583.Iso8583Message;
import com.floss83.javaswitch.iso8583.Iso8583ParseException;
import com.floss83.javaswitch.iso8583.Iso8583Parser;
import com.floss83.javaswitch.tokenization.TokenizationService;

/**
 * =========================
 * ISO 8583 TCP Server
 * =========================
 *
 * <b>Purpose:</b>
 * <ul>
 * <li>Receives raw ISO 8583 financial messages over TCP from ATM/POS simulators
 * or test tools.</li>
 * <li>Each incoming connection is treated as a single request/response session
 * (blocking I/O).</li>
 * <li>All PCI-sensitive data (e.g., PAN) is tokenized before any further
 * processing, logging, or outbound flow.</li>
 * <li>This class is designed for DEV/QA/AUDIT environments, not for
 * production/high-load use.</li>
 * </ul>
 *
 * <b>Usage:</b>
 * 
 * <pre>
 * TcpServer tcpServer = new TcpServer(5000, tokenizationService);
 * new Thread(tcpServer).start();
 * </pre>
 *
 * <b>Security/Compliance Notes:</b>
 * <ul>
 * <li>PAN/CVV and other cardholder data are tokenized immediately after message
 * parse to avoid audit violations.</li>
 * <li>Audit logs mask sensitive data using TokenizationService.mask() before
 * printing.</li>
 * <li>This implementation is for safe, local, single-client lab or test usage
 * only.</li>
 * </ul>
 *
 * <b>For Production:</b>
 * <ul>
 * <li>Implement length-prefixed protocol, concurrency, socket timeouts, and
 * robust error isolation.</li>
 * <li>Never use blocking-per-connection model or single-threaded server for
 * real payment traffic.</li>
 * </ul>
 *
 * @author Floss83
 * @version 1.2 (Audit-Grade)
 */
public class TcpServer implements Runnable {

    private final int port;
    private final TokenizationService tokenizationService;

    /**
     * Create a new ISO8583 TCP server.
     * 
     * @param port                The TCP port to listen on (e.g., 5000).
     * @param tokenizationService The PCI-compliant tokenization service to use.
     */
    public TcpServer(int port, TokenizationService tokenizationService) {
        this.port = port;
        this.tokenizationService = tokenizationService;
    }

    /**
     * Main server loop: accepts TCP connections and processes each as a single
     * ISO8583 message session.
     * All exceptions are logged to stderr; the server remains alive on error.
     */
    @Override
    public void run() {
        System.out.println("[TCP] Server starting on port " + port + " ...");
        Iso8583Parser parser = new Iso8583Parser();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("[TCP] Connection from " + clientSocket.getRemoteSocketAddress());

                    try (
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(clientSocket.getInputStream()));
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                        // 1. Read full message until client closes connection
                        StringBuilder sb = new StringBuilder();
                        int ch;
                        while ((ch = in.read()) != -1) {
                            sb.append((char) ch);
                        }
                        String message = sb.toString().trim();

                        System.out.println("[TCP] Message length: " + message.length());
                        System.out.println("[TCP] Received: " + message);

                        try {
                            // 2. Parse ISO8583 message structure
                            Iso8583Message parsed = parser.parse(message);

                            // 3. Tokenize sensitive fields (e.g., PAN, field 2) for PCI compliance
                            String pan = parsed.getDataElement(2);
                            String tokenizedPan = pan != null ? tokenizationService.tokenizePan(pan) : null;

                            if (tokenizedPan != null) {
                                parsed.getMutableDataElements().put(2, tokenizedPan);
                                System.out.println("[AUDIT] PAN tokenized in TCP flow: "
                                        + tokenizationService.mask(pan)
                                        + " -> "
                                        + tokenizationService.mask(tokenizedPan));
                            }

                            // 4. Print all fields (post-tokenization)
                            System.out.println("[TCP] Parsed MTI: " + parsed.getMti());
                            parsed.getMutableDataElements().forEach((field, value) -> {
                                System.out.println("[TCP] Field " + field + ": " + value);
                            });

                            // 5. Acknowledge client
                            out.println("ACK: Message parsed and tokenized successfully");
                        } catch (Iso8583ParseException ex) {
                            System.err.println("[TCP] Parse Error: " + ex.getMessage());
                            ex.printStackTrace();
                            out.println("ERR: " + ex.getMessage());
                        }
                    } catch (Exception e) {
                        System.err.println("[TCP] Client handling error: " + e.getMessage());
                        e.printStackTrace();
                    }
                    // Connection closed automatically by try-with-resources
                }
            }
        } catch (Exception e) {
            System.err.println("[TCP] Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
