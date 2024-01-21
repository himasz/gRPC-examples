package grpc.examples.basic;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(3456)
                .addService(new GrpcSimpleService())
                .build();
        server.start();
        System.out.println("Successfully Started the Server");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            System.out.println("Successfully Stopped the server");
        }));
        server.awaitTermination();
    }
}
