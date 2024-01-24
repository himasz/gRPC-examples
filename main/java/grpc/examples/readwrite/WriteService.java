package grpc.examples.readwrite;

import com.opencsv.CSVWriter;
import io.grpc.stub.StreamObserver;
import service.write.WriteDataRequest;
import service.write.WriteDataResponse;
import service.write.WriteServiceGrpc;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

public class WriteService extends WriteServiceGrpc.WriteServiceImplBase {

    boolean isHeadersAdded = false;
    private final AtomicReference<WriteDataRequest> lastWrite;

    public WriteService(AtomicReference<WriteDataRequest> lastWrite) {
        super();
        this.lastWrite = lastWrite;
    }

    @Override
    public StreamObserver<WriteDataRequest> write(StreamObserver<WriteDataResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(WriteDataRequest request) {
                String fileName = "main/resources/data.csv";
                try (CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true))) {
                    if (isCsvFileEmpty(fileName) || !isHeadersAdded) {
                        String[] header = {"timestamp", "energy"};
                        csvWriter.writeNext(header);
                        isHeadersAdded = true;
                    }
                    double energy = request.getEnergy();
                    long timestamp = request.getTimestamp();
                    csvWriter.writeNext(new String[]{String.valueOf(timestamp), String.valueOf(energy)});
                    lastWrite.set(WriteDataRequest.newBuilder().setEnergy(energy).setTimestamp(timestamp).build());
                    System.out.println("Data has been written to " + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in client streaming: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(WriteDataResponse.newBuilder().setResponse("writing is done").build());
                responseObserver.onCompleted();
            }
        };
    }

    private static boolean isCsvFileEmpty(String fileName) throws IOException {
        Path filePath = Paths.get(fileName);
        long fileSize = Files.size(filePath);
        return fileSize == 0;
    }
}
