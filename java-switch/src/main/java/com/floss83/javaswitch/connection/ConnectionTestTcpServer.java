package com.floss83.javaswitch.connection;

public class ConnectionTestTcpServer {
    public static void main(String[] args) {
        // Start only the TCP server for local testing on port 5000
        new Thread(new TcpServer(5000)).start();

        // Keep the main thread alive, or the process will exit!
        // (You can also use Thread.sleep(Long.MAX_VALUE) in a pinch)
        while (true) {
            try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
        }
    }
}
