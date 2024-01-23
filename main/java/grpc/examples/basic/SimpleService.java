package grpc.examples.basic;

import io.grpc.stub.StreamObserver;
import service.simple.*;
import service.simple.SimpleServiceGrpc.SimpleServiceImplBase;

public class SimpleService extends SimpleServiceImplBase {
    @Override
    public void helloV1(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        responseObserver.onNext(HelloResponse.newBuilder().setMessage("HelloV1 - Hello " + request.getMessage()).build());
        //io.grpc.StatusRuntimeException: CANCELLED: RST_STREAM closed stream. HTTP/2 error code: CANCEL
        //responseObserver.onNext(HelloResponse.newBuilder().setMessage("Last!").build());
        responseObserver.onCompleted();
    }

    @Override
    public void helloV2(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        responseObserver.onNext(HelloResponse.newBuilder().setMessage("HelloV2 - Hello ").build());
        responseObserver.onNext(HelloResponse.newBuilder().setMessage("HelloV2 - " + request.getMessage()).build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloRequest> helloV3(StreamObserver<HelloResponse> responseObserver) {
        return new StreamObserver<>() {
            private final StringBuilder clientMessages = new StringBuilder();

            @Override
            public void onNext(HelloRequest request) {
                //We could add some calculations here like averages, std and other things!
                clientMessages.append(request.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in client streaming: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(HelloResponse.newBuilder().setMessage(clientMessages.toString()).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<HelloRequest> helloV4(StreamObserver<HelloResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloRequest request) {
                //ofc we could do something here!
                responseObserver.onNext(HelloResponse.newBuilder().setMessage(request.getMessage()).build());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in client streaming: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
