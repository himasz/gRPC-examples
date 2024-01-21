package grpc.raw;

import grpc.raw.dto.KillTheBufferRequestDto;
import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RawGrpcClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress("localhost", 5678).usePlaintext();
        ManagedChannel channel = channelBuilder.build();

        final CountDownLatch finishLatch = new CountDownLatch(4);
        ClientCalls.asyncServerStreamingCall(channel.newCall(RawGrpcSimpleService.HELLO, CallOptions.DEFAULT), "Ebrahim", new StreamObserver<String>() {
            @Override
            public void onNext(String greeting) {
                System.out.println(greeting);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        });

        for (int i = 0; i < 3; i++) {
            long time = System.currentTimeMillis();
            ClientCalls.asyncServerStreamingCall(channel.newCall(RawGrpcSimpleService.KILL_THE_BUFFER, CallOptions.DEFAULT), new KillTheBufferRequestDto("message"), new StreamObserver<Integer>() {
                @Override
                public void onNext(Integer killTheBufferResponseDto) {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    System.out.println("    time: " + (System.currentTimeMillis() - time));
                    finishLatch.countDown();
                }
            });
        }
        finishLatch.await(1, TimeUnit.MINUTES);


    }
}
