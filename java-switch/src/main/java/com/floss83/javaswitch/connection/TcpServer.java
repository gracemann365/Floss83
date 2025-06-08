package com.floss83.javaswitch.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.floss83.javaswitch.iso8583.Iso8583Message;
import com.floss83.javaswitch.iso8583.Iso8583ParseException;
import com.floss83.javaswitch.iso8583.Iso8583Parser;

/**
 * TcpServer is a simple, blocking TCP server designed to receive raw ISO 8583
 * messages
 * from POS/ATM simulator clients over a specified port. Each incoming
 * connection is treated
 * as a single message session: all data is read, parsed, and acknowledged once
 * per connection.
 * <p>
 * Usage example (run in a separate thread):
 * 
 * <pre>
 * TcpServer tcpServer = new TcpServer(5000);
 * new Thread(tcpServer).start();
 * </pre>
 * <p>
 * This implementation is single-threaded and suitable for local testing only.
 * For production, use a length-prefixed protocol or non-blocking I/O.
 * </p>
 *
 * @author Floss83
 * @version 1.1
 * 
 *          How this fixes it:
 *          Reads entire message as one string per connection (no splitting on
 *          \n, no duplicate reads).
 * 
 *          Works with any TCP client, including Python, telnet, netcat.
 * 
 *          message.length() should print 82 for the sample test string.
 * 
 * 
 */
public class TcpServer implements Runnable {

    private final int port;

    public TcpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("[TCP] Server starting on port " + port + " ...");
        Iso8583Parser parser = new Iso8583Parser();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[TCP] Connection from " + clientSocket.getRemoteSocketAddress());
                try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    // Read all data sent over this connection (one message per connection)
                    StringBuilder sb = new StringBuilder();
                    int ch;
                    while ((ch = in.read()) != -1) {
                        sb.append((char) ch);
                    }
                    String message = sb.toString().trim();

                    System.out.println("[TCP] Message length: " + message.length());
                    System.out.println("[TCP] Received: " + message);

                    try {
                        Iso8583Message parsed = parser.parse(message);
                        System.out.println("[TCP] Parsed MTI: " + parsed.getMti());
                        parsed.getDataElements().forEach((field, value) -> {
                            System.out.println("[TCP] Field " + field + ": " + value);
                        });
                        out.println("ACK: Message parsed successfully");
                    } catch (Iso8583ParseException ex) {
                        System.err.println("[TCP] Parse Error: " + ex.getMessage());
                        ex.printStackTrace();
                        out.println("ERR: " + ex.getMessage());
                    }
                } catch (Exception e) {
                    System.err.println("[TCP] Client handling error: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    clientSocket.close();
                }
            }
        } catch (Exception e) {
            System.err.println("[TCP] Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
