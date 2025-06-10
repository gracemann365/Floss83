package com.floss83.javaswitch.connection.connectiontests;

import com.floss83.javaswitch.connection.TcpServer;
import com.floss83.javaswitch.tokenization.TokenizationService;

/**
 * 
 * Standalone connection class for the switch. This class is used to connect to
 * the switch and perform operations on it.
 * smoke bomb testing
 * NOW
 * tcp server integrated to spring boot app
 */
public class ConnectionTestTcpServer {

    private static TokenizationService tokenizationService;

    public static void main(String[] args) {
        // Start only the TCP server for local testing on port 5000
        new Thread(new TcpServer(5000, tokenizationService)).start();

        // Keep the main thread alive, or the process will exit!
        // (You can also use Thread.sleep(Long.MAX_VALUE) in a pinch)
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
