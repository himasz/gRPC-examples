package grpc.examples.readwrite;

import io.grpc.stub.StreamObserver;
import service.read.ReadRequest;
import service.read.ReadResponse;
import service.read.ReadServiceGrpc;
import service.write.WriteDataRequest;

import java.util.concurrent.atomic.AtomicReference;

public class ReadService extends ReadServiceGrpc.ReadServiceImplBase {
    private final AtomicReference<WriteDataRequest> lastWrite;

    public ReadService(AtomicReference<WriteDataRequest> lastWrite) {
        super();
        this.lastWrite = lastWrite;
    }

    @Override
    public void read(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
        ReadResponse response = ReadResponse
                .newBuilder()
                .setEnergy(lastWrite.get().getEnergy())
                .setTimestamp(lastWrite.get().getTimestamp())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
