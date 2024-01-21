package grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import simpleservice.curalie.*;
import simpleservice.curalie.SimpleServiceGrpc.SimpleServiceStub;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GrpcClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress("localhost", 3456).usePlaintext();
        ManagedChannel channel = channelBuilder.build();
        SimpleServiceStub serviceStub = SimpleServiceGrpc.newStub(channel);

        final CountDownLatch finishLatch = new CountDownLatch(5);

        serviceStub.hello(HelloRequest.newBuilder().setName("Ebrahim").build(), new StreamObserver<>() {
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

        ArgsExampleRequest argsExampleRequest = ArgsExampleRequest
                .newBuilder()
                .setMessage("Message")
                .setX("X1")
                .setY(13)
                .build();

        serviceStub.argsExample(argsExampleRequest, new StreamObserver<>() {
            @Override
            public void onNext(ArgsExampleResponse argsExampleResponse) {
                System.out.println(argsExampleResponse);
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

        for (int i = 0; i < 3; i++) {
                System.out.println("starting killTheBuffer round: " + i);
                long tim = System.currentTimeMillis();
                AtomicInteger cnt = new AtomicInteger();
                serviceStub.killTheBuffer(KillTheBufferRequest.newBuilder().build(), new StreamObserver<>() {
                    @Override
                    public void onNext(KillTheBufferResponse killTheBufferResponse) {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println(throwable);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("    time: " + (System.currentTimeMillis() - tim));
                        finishLatch.countDown();
                    }
                });
        }
        finishLatch.await(1, TimeUnit.MINUTES);

    }
}
