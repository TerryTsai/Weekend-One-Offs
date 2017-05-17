package jserve.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;

public class Connector implements Runnable {

    private volatile boolean isRunning;
    private final ServerSocket server;
    private final BlockingQueue<Socket> clients;

    public Connector(ServerSocket server, BlockingQueue<Socket> clients) {
        this.isRunning = true;
        this.clients = clients;
        this.server = server;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Socket socket = server.accept();
                if (socket == null) continue;
                clients.add(socket);
            } catch (SocketTimeoutException e) {
                // Expected exception;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        this.isRunning = false;
    }

}
