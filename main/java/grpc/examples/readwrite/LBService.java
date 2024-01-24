package grpc.examples.readwrite;

import io.grpc.stub.StreamObserver;
import service.lb.FreeServiceRequest;
import service.lb.FreeServiceResponse;
import service.lb.LBServiceGrpc;

public class LBService extends LBServiceGrpc.LBServiceImplBase {

    @Override
    public void freeService(FreeServiceRequest request, StreamObserver<FreeServiceResponse> responseObserver) {
        String response = "localhost:";
        if (request.getService().equals("write")) {
            response += "123";
        } else if (request.getService().equals("read")) {
            response += "124";
        }
        responseObserver.onNext(FreeServiceResponse.newBuilder().setResponse(response).build());
        responseObserver.onCompleted();
    }

}
