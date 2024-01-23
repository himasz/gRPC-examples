package grpc.examples.bookstore;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class BookstoreServer {

    public static void main(String[] args) throws Exception {
        // Create a new server listening on port 9090
        Server server = ServerBuilder.forPort(1234)
                .addService(new BookstoreServiceImpl())
                .build();

        // Start the server
        server.start();

        // Block to keep the server running
        server.awaitTermination();
    }
}
