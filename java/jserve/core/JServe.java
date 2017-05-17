package jserve.core;

import jserve.processors.Processor;
import jserve.utils.ExceptionUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class JServe implements Runnable {

    private final BlockingQueue<Socket> clients;
    private final List<Processor> preprocessors;
    private final Map<String, Processor> endpoints;
    private final ExecutorService workers;
    private final ServerSocket server;
    private final Connector connector;
    private final Dispatcher dispatcher;

    private final Thread connectorThread;
    private final Thread dispatcherThread;

    public JServe() throws IOException {
        clients = new LinkedBlockingQueue<>();
        endpoints = new LinkedHashMap<>();
        preprocessors = new ArrayList<>();
        workers = Executors.newFixedThreadPool(100);

        server = new ServerSocket();
        server.setSoTimeout(3000);
        server.bind(new InetSocketAddress("localhost", 8080));

        connector = new Connector(server, clients);
        connectorThread = new Thread(connector);

        dispatcher = new Dispatcher(clients, workers, preprocessors, endpoints);
        dispatcherThread = new Thread(dispatcher);
    }

    @Override
    public void run() {
        System.out.println("Startup...");
        startup();

        System.out.println("Press Enter To Exit...");
        pause();

        System.out.println("Shutdown...");
        shutdown();
    }

    private void startup() {
        System.out.println("Starting Connector...");
        connectorThread.start();

        System.out.println("Starting Dispatcher...");
        dispatcherThread.start();
    }

    private void pause() {
        ExceptionUtils.run(() -> {
            while (System.in.read() != 10) {
            }
        });
    }

    private void shutdown() {
        System.out.println("Closing Dispatcher...");
        dispatcher.shutdown();
        ExceptionUtils.join(dispatcherThread);

        System.out.println("Closing Connector...");
        connector.shutdown();
        ExceptionUtils.join(connectorThread);

        System.out.println("Closing Server Socket...");
        ExceptionUtils.close(server);

        System.out.println("Closing Client Sockets...");
        clients.forEach(ExceptionUtils::close);

        System.out.println("Closing Worker Threads...");
        workers.shutdown();
    }

    public void addPreprocessor(Processor processor) {
        preprocessors.add(processor);
    }

    public void addEndpoint(String regex, Processor processor) {
        endpoints.put(regex, processor);
    }
}
