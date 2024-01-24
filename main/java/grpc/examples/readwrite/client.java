package grpc.examples.readwrite;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import service.lb.FreeServiceRequest;
import service.lb.FreeServiceResponse;
import service.lb.LBServiceGrpc;
import service.read.ReadRequest;
import service.read.ReadResponse;
import service.read.ReadServiceGrpc;
import service.write.WriteDataRequest;
import service.write.WriteDataResponse;
import service.write.WriteServiceGrpc;

import java.util.concurrent.CountDownLatch;

import static grpc.examples.readwrite.Application.LB_PORT;

public class Client {

    public static final String SPLITTER = ":";
    public static final String READ = "read";
    public static final String WRITE = "write";

    public static void main(String[] args) throws InterruptedException {
        //LB
        ManagedChannel lbChannel = createChannel("localhost", LB_PORT);
        LBServiceGrpc.LBServiceBlockingStub lbBlockingStub = LBServiceGrpc.newBlockingStub(lbChannel);

        FreeServiceResponse readService =
                lbBlockingStub.freeService(FreeServiceRequest.newBuilder().setService(READ).build());
        FreeServiceResponse writeService =
                lbBlockingStub.freeService(FreeServiceRequest.newBuilder().setService(WRITE).build());

        String[] writeServiceSplit = writeService.getResponse().split(SPLITTER);
        ManagedChannel writeChannel = createChannel(writeServiceSplit[0], Integer.parseInt(writeServiceSplit[1]));

        //Write Service
        final CountDownLatch latch = new CountDownLatch(1);
        WriteServiceGrpc.WriteServiceStub writeServiceStub = WriteServiceGrpc.newStub(writeChannel);
        StreamObserver<WriteDataRequest> requestStreamObserver = writeServiceStub.write(createServerResponseObserver(latch));
        requestStreamObserver.onNext(WriteDataRequest.newBuilder().setTimestamp(System.currentTimeMillis()).setEnergy(11.1).build());
        requestStreamObserver.onNext(WriteDataRequest.newBuilder().setTimestamp(System.currentTimeMillis()).setEnergy(12.4).build());
        requestStreamObserver.onCompleted();
        latch.await();

        //READ Service
        String[] readServiceSplit = readService.getResponse().split(SPLITTER);
        ManagedChannel readChannel = createChannel(readServiceSplit[0], Integer.parseInt(readServiceSplit[1]));
        ReadServiceGrpc.ReadServiceBlockingStub readServiceBlockingStub = ReadServiceGrpc.newBlockingStub(readChannel);
        ReadResponse read = readServiceBlockingStub.read(ReadRequest.newBuilder().setTimestamp(System.currentTimeMillis()).build());
        System.out.println(read);
    }

    private static StreamObserver<WriteDataResponse> createServerResponseObserver(CountDownLatch latch) {
        return new StreamObserver<>() {
            @Override
            public void onNext(WriteDataResponse response) {
                System.out.println("Received server response: " + response.getResponse());
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

    private static ManagedChannel createChannel(String localhost, int lbPort) {
        return ManagedChannelBuilder
                .forAddress(localhost, lbPort)
                .usePlaintext()
                .build();
    }

}
