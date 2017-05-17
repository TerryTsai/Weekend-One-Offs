package jserve.core;

import jserve.model.Request;
import jserve.processors.Processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Dispatcher implements Runnable {

    private volatile boolean isRunning;
    private final long timeout;
    private final BlockingQueue<Socket> clients;
    private final ExecutorService workers;
    private final List<Processor> preProcessors;
    private final Map<String, Processor> processors;

    public Dispatcher(BlockingQueue<Socket> clients, ExecutorService workers,
                      List<Processor> preProcessors, Map<String, Processor> processors) {
        this.timeout = 1000;
        this.isRunning = true;
        this.clients = clients;
        this.workers = workers;
        this.preProcessors = preProcessors;
        this.processors = processors;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Socket client = clients.poll(timeout, TimeUnit.MILLISECONDS);
                if (client == null) continue;
                workers.submit(newJob(client));
            } catch (InterruptedException e) {
                // To prevent hangups
            }
        }
    }

    public void shutdown() {
        this.isRunning = false;
    }

    private Runnable newJob(Socket client) {
        return () -> {
            try {
                Request request = new Request();
                InputStream input = client.getInputStream();
                OutputStream output = client.getOutputStream();
                request.read(input);

                for (Processor preProcessor : preProcessors)
                    preProcessor.process(request, input, output);

                for (Map.Entry<String, Processor> endpoint : processors.entrySet())
                    if (request.getUri().matches(endpoint.getKey())) {
                        endpoint.getValue().process(request, input, output);
                        output.flush();
                        break;
                    }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try { client.close(); } catch (IOException e) {}
            }
        };
    }

}
