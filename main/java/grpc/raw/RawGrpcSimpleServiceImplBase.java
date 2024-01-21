package grpc.raw;

import grpc.raw.dto.KillTheBufferRequestDto;
import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;

import static grpc.raw.MarshallerUtils.createMarshaller;

public abstract class RawGrpcSimpleServiceImplBase implements BindableService {
    private static final String SERVICE_NAME = "io.grpc.SimpleService";

    public abstract void hello(String name, StreamObserver<String> responseObserver);
    public abstract void killTheBuffer(KillTheBufferRequestDto request, StreamObserver<Integer> responseObserver);

    @Override
    public final ServerServiceDefinition bindService() {
        ServerServiceDefinition.Builder ssd = ServerServiceDefinition.builder(SERVICE_NAME);
        ssd.addMethod(HELLO, ServerCalls.asyncServerStreamingCall(this::hello));
        ssd.addMethod(KILL_THE_BUFFER, ServerCalls.asyncServerStreamingCall(this::killTheBuffer));
        return ssd.build();
    }

    static final MethodDescriptor<KillTheBufferRequestDto, Integer> KILL_THE_BUFFER =
            MethodDescriptor.newBuilder(
                            createMarshaller(KillTheBufferRequestDto.class),
                            createMarshaller(Integer.class))
                    .setFullMethodName(
                            MethodDescriptor.generateFullMethodName(SERVICE_NAME, "killTheBuffer"))
                    .setType(MethodDescriptor.MethodType.SERVER_STREAMING)
                    .setSampledToLocalTracing(true)
                    .build();
    static final MethodDescriptor<String, String> HELLO =
            MethodDescriptor.newBuilder(
                            createMarshaller(String.class),
                            createMarshaller(String.class))
                    .setFullMethodName(
                            MethodDescriptor.generateFullMethodName(SERVICE_NAME, "hello"))
                    .setType(MethodDescriptor.MethodType.SERVER_STREAMING)
                    .setSampledToLocalTracing(true)
                    .build();

}

