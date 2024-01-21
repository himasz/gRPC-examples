package grpc.examples;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import service.simple.HelloRequest;
import service.simple.HelloResponse;
import service.simple.SimpleServiceGrpc;
import service.simple.SimpleServiceGrpc.SimpleServiceStub;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GrpcBlockingClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder
                .forAddress("localhost", 3456)
                .usePlaintext();
        ManagedChannel channel = channelBuilder.build();
        SimpleServiceGrpc.SimpleServiceBlockingStub simpleServiceBlockingStub = SimpleServiceGrpc.newBlockingStub(channel);
        System.out.println(simpleServiceBlockingStub.helloV1(HelloRequest.newBuilder().setMessage("Ebrahim").build()));

        Iterator<HelloResponse> iterator
                = simpleServiceBlockingStub.helloV2(HelloRequest.newBuilder().setMessage("Ebrahim").build());
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }

}
