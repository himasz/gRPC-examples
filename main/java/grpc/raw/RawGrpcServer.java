package grpc.raw;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class RawGrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server =
                ServerBuilder.forPort(5678)
                        .addService(new RawGrpcSimpleService())
                        .build();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            System.out.println("Successfully stopped the server");
        }));
        server.awaitTermination();
    }

}
