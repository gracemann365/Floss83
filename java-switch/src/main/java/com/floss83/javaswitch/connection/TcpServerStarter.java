package com.floss83.javaswitch.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.floss83.javaswitch.tokenization.TokenizationService;

import jakarta.annotation.PostConstruct;

@Component
public class TcpServerStarter {

    private final TokenizationService tokenizationService;

    @Autowired
    public TcpServerStarter(TokenizationService tokenizationService) {
        this.tokenizationService = tokenizationService;
    }

    @PostConstruct
    public void startTcpServer() {
        int port = 5000; // Or inject via config
        Thread tcpThread = new Thread(new TcpServer(port, tokenizationService));
        tcpThread.setDaemon(true); // Doesn't block Spring Boot shutdown
        tcpThread.start();
        System.out.println("[BOOT] TCP Server thread started on port " + port);
    }
}
