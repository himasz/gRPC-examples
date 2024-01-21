package grpc.examples.basic;

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
        SimpleServiceGrpc.SimpleServiceBlockingStub simpleServiceBlockingStub =
                SimpleServiceGrpc.newBlockingStub(channelBuilder.build());

        HelloResponse response =
                simpleServiceBlockingStub.helloV1(HelloRequest.newBuilder().setMessage("Ebrahim").build());

        System.out.println(response);

        Iterator<HelloResponse> iterator =
                simpleServiceBlockingStub.helloV2(HelloRequest.newBuilder().setMessage("Ebrahim").build());
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }

}
