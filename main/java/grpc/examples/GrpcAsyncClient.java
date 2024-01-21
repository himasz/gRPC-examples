package grpc.examples;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import service.simple.HelloRequest;
import service.simple.HelloResponse;
import service.simple.SimpleServiceGrpc;
import service.simple.SimpleServiceGrpc.SimpleServiceStub;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GrpcAsyncClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder
                .forAddress("localhost", 3456)
                .usePlaintext();
        ManagedChannel channel = channelBuilder.build();

        SimpleServiceStub serviceStub = SimpleServiceGrpc.newStub(channel);
        final CountDownLatch latch = new CountDownLatch(4);

        callHelloV1(serviceStub, latch);
        callHelloV2(serviceStub, latch);
        callHelloV3(serviceStub, latch);
        callHelloV4(serviceStub, latch);


        latch.await(1, TimeUnit.MINUTES);

    }

    private static void callHelloV1(SimpleServiceStub serviceStub, CountDownLatch finishLatch) {
        serviceStub.helloV1(HelloRequest.newBuilder().setMessage("Ebrahim").build(), new StreamObserver<>() {
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

    private static void callHelloV2(SimpleServiceStub serviceStub, CountDownLatch finishLatch) {
        serviceStub.helloV2(HelloRequest.newBuilder().setMessage("Ebrahim").build(), new StreamObserver<>() {
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

    private static void callHelloV3(SimpleServiceStub serviceStub, CountDownLatch latch) {
        StreamObserver<HelloRequest> requestStreamObserver = serviceStub.helloV3(createServerResponseObserver(latch));
        requestStreamObserver.onNext(HelloRequest.newBuilder().setMessage("EbrahimV3 ").build());
        requestStreamObserver.onNext(HelloRequest.newBuilder().setMessage("ZidanV3").build());
        requestStreamObserver.onCompleted();
    }
    private static void callHelloV4(SimpleServiceStub serviceStub, CountDownLatch latch) {
        StreamObserver<HelloRequest> requestStreamObserver = serviceStub.helloV4(createServerResponseObserver(latch));
        requestStreamObserver.onNext(HelloRequest.newBuilder().setMessage("EbrahimV4 ").build());
        requestStreamObserver.onNext(HelloRequest.newBuilder().setMessage("ZidanV4").build());
        requestStreamObserver.onCompleted();
    }

    private static StreamObserver<HelloResponse> createServerResponseObserver(CountDownLatch latch) {
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse response) {
                System.out.println("Received server response: " + response.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in client streaming: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                latch.countDown();
                System.out.println("Client streaming completed");
            }
        };
    }
}
