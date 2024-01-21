package grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import service.simple.HelloRequest;
import service.simple.HelloResponse;
import service.simple.SimpleServiceGrpc;
import service.simple.SimpleServiceGrpc.SimpleServiceStub;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GrpcClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder
                .forAddress("localhost", 3456)
                .usePlaintext();
        ManagedChannel channel = channelBuilder.build();

        SimpleServiceStub serviceStub = SimpleServiceGrpc.newStub(channel);
        final CountDownLatch latch = new CountDownLatch(3);

        callHelloV1(serviceStub, latch);
        callHelloV2(serviceStub, latch);
        callHelloV3(serviceStub, latch);


        latch.await(1, TimeUnit.MINUTES);

    }

    private static void callHelloV3(SimpleServiceStub serviceStub, CountDownLatch latch) {
        StreamObserver<HelloRequest> requestStreamObserver = serviceStub.helloV3(createServerResponseObserver(latch));
        requestStreamObserver.onNext(HelloRequest.newBuilder().setName("Ebrahim ").build());
        requestStreamObserver.onNext(HelloRequest.newBuilder().setName("Zidan").build());
        requestStreamObserver.onCompleted();
    }

    private static StreamObserver<HelloResponse> createServerResponseObserver(CountDownLatch latch) {
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse response) {
                System.out.println("HelloV3 - Received server response: " + response.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("HelloV3 - Error in client streaming: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                latch.countDown();
                System.out.println("HelloV3 - Client streaming completed");
            }
        };
    }

    private static void callHelloV2(SimpleServiceStub serviceStub, CountDownLatch finishLatch) {
        serviceStub.helloV2(HelloRequest.newBuilder().setName("Ebrahim").build(), new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                System.out.println(helloResponse.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        });
    }

    private static void callHelloV1(SimpleServiceStub serviceStub, CountDownLatch finishLatch) {
        serviceStub.helloV1(HelloRequest.newBuilder().setName("Ebrahim").build(), new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                System.out.println(helloResponse.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        });
    }
}
