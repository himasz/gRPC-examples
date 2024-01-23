package grpc.examples.bookstore;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import service.bookstore.*;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BookstoreClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 1234)
                .usePlaintext() // Use plaintext communication for simplicity (insecure, for demo purposes)
                .build();

        // Create a synchronous stub
        BookstoreServiceGrpc.BookstoreServiceBlockingStub blockingStub = BookstoreServiceGrpc.newBlockingStub(channel);

        // Task 2: Demonstrate how the client can call each of the RPC methods

        // 2.1 ListBooks
        System.out.println("ListBooks:");
        ListBooksResponse listBooksResponse = blockingStub.listBooks(ListBooksRequest.newBuilder().build());
        listBooksResponse.getBookList().forEach(book ->
                System.out.println("Book ID: " + book.getId() +
                        ", Title: " + book.getTitle() +
                        ", Author: " + book.getAuthor()));

        // 2.2 GetBookDetails
        System.out.println("\nGetBookDetails:");
        GetBookDetailsResponse getBookDetailsResponse = blockingStub.getBookDetails(GetBookDetailsRequest.newBuilder().setBookId("1").build());
        System.out.println("Title: " + getBookDetailsResponse.getTitle() +
                ", Author: " + getBookDetailsResponse.getAuthor() +
                ", Description: " + getBookDetailsResponse.getDescription());

        // 2.3 GetBookReviews
        System.out.println("\nGetBookReviews:");
        Iterator<BookReview> bookReviewIterator = blockingStub.getBookReviews(GetBookReviewsRequest.newBuilder().setBookId("1").build());
        while (bookReviewIterator.hasNext()) {
            BookReview review = bookReviewIterator.next();
            System.out.println("Reviewer: " + review.getReviewerName() + ", Review: " + review.getReviewText());

        }

        // 2.4 SubmitBookReview
        System.out.println("\nSubmitBookReview:");
        BookstoreServiceGrpc.BookstoreServiceStub serviceStub = BookstoreServiceGrpc.newStub(channel);
//        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<SubmitBookReviewRequest> requestStreamObserver =
                serviceStub.submitBookReview(new StreamObserver<>() {
                    @Override
                    public void onNext(SubmitBookReviewResponse response) {
                        System.out.println("Response: " + response.getMessage());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("Error in submitting book review: " + t.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        // This will be called when the server completes the RPC call
                        System.out.println("SubmitBookReview completed");
//                        latch.countDown();
                    }
                });

        // Send a sample review
        requestStreamObserver.onNext(SubmitBookReviewRequest.newBuilder()
                .setBookId("1")
                .setReviewerName("John")
                .setReviewText("Enjoyed reading it!")
                .build());

        // Complete the RPC call
        requestStreamObserver.onCompleted();
//        latch.await(10, TimeUnit.SECONDS);

        // Wait for a moment to see the asynchronous completion message
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shutdown the channel
        channel.shutdown();
    }
}
