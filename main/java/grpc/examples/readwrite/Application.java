package grpc.examples.readwrite;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.write.WriteDataRequest;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class Application {

    public static final int LB_PORT = 121;
    public static final int WRITE_PORT = 123;
    public static final int READ_PORT = 124;

    public static void main(String[] args) throws IOException, InterruptedException {
        AtomicReference<WriteDataRequest> lastWrite = new AtomicReference<>();
        Server lbServer = creatServer(LB_PORT, new LBService());
        Server readServer = creatServer(READ_PORT, new ReadService(lastWrite));
        Server writeServer = creatServer(WRITE_PORT, new WriteService(lastWrite));

        startServers(lbServer, writeServer, readServer);

        System.out.println("Successfully Started the Servers");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutDownServers(lbServer, writeServer, readServer);
            System.out.println("Successfully Stopped the Servers");
        }));
        lbServer.awaitTermination();
        readServer.awaitTermination();
    }

    private static Server creatServer(int port, BindableService service) {
        return ServerBuilder.forPort(port)
                .addService(service)
                .build();
    }

    private static void startServers(Server lbServer, Server writeServer, Server readServer) throws IOException {
        lbServer.start();
        readServer.start();
        writeServer.start();
    }

    private static void shutDownServers(Server lbServer, Server writeServer, Server readServer) {
        lbServer.shutdown();
        readServer.shutdown();
        writeServer.shutdown();
    }

}
