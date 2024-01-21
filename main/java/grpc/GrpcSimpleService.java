package grpc;

import io.grpc.stub.StreamObserver;
import service.simple.*;
import service.simple.SimpleServiceGrpc.SimpleServiceImplBase;

public class GrpcSimpleService extends SimpleServiceImplBase {
    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        responseObserver.onNext(HelloResponse.newBuilder().setMessage("Hello " + request.getName()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void argsExample(ArgsExampleRequest request, StreamObserver<ArgsExampleResponse> responseObserver) {
        StringBuilder response = new StringBuilder("Args - Message: ");
        response.append( request.getMessage());
        response.append( " X: ");
        response.append(request.getX());
        response.append( " Y: ");
        response.append(request.getY());

        responseObserver.onNext(ArgsExampleResponse.newBuilder().setResponse(response.toString()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void killTheBuffer(KillTheBufferRequest request, StreamObserver<KillTheBufferResponse> responseObserver) {
        for (int i = 0; i < 100_000; i++) {
            responseObserver.onNext(KillTheBufferResponse.newBuilder().setCount(i).build());
        }
        responseObserver.onCompleted();

    }
}
