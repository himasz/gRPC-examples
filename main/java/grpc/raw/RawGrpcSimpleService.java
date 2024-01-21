package grpc.raw;

import grpc.raw.dto.KillTheBufferRequestDto;
import io.grpc.stub.StreamObserver;

public class RawGrpcSimpleService extends RawGrpcSimpleServiceImplBase {

    @Override
    public void hello(String name, StreamObserver<String> responseObserver) {
        responseObserver.onNext("Hello " + name);
        responseObserver.onCompleted();
    }

    @Override
    public void killTheBuffer(KillTheBufferRequestDto request, StreamObserver<Integer> responseObserver) {
        for (int i = 0; i < 100_000; i++) {
            responseObserver.onNext(i);
        }
        responseObserver.onCompleted();

    }
}
