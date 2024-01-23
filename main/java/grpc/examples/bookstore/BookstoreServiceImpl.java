package grpc.examples.bookstore;

import io.grpc.stub.StreamObserver;
import service.bookstore.*;

public class BookstoreServiceImpl extends BookstoreServiceGrpc.BookstoreServiceImplBase {

        // Sample data for books and reviews
        private static final Book book1 = Book.newBuilder()
                .setId("1")
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setDescription("A novel about the American Dream")
                .build();

        private static final Book book2 = Book.newBuilder()
                .setId("2")
                .setTitle("To Kill a Mockingbird")
                .setAuthor("Harper Lee")
                .setDescription("A classic novel of modern American literature")
                .build();

        private static final BookReview review1 = BookReview.newBuilder()
                .setReviewerName("Alice")
                .setReviewText("Great book! Loved the characters.")
                .build();

        private static final BookReview review2 = BookReview.newBuilder()
                .setReviewerName("Bob")
                .setReviewText("Interesting plot twists.")
                .build();

        @Override
        public void listBooks(ListBooksRequest request, StreamObserver<ListBooksResponse> responseObserver) {
            ListBooksResponse response = ListBooksResponse.newBuilder()
                    .addBook(book1)
                    .addBook(book2)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getBookDetails(GetBookDetailsRequest request, StreamObserver<BookDetailsResponse> responseObserver) {
            if ("1".equals(request.getBookId())) {
                BookDetailsResponse response = BookDetailsResponse.newBuilder()
                        .setTitle(book1.getTitle())
                        .setAuthor(book1.getAuthor())
                        .setDescription(book1.getDescription())
                        .build();
                responseObserver.onNext(response);
            } else if ("2".equals(request.getBookId())) {
                BookDetailsResponse response = BookDetailsResponse.newBuilder()
                        .setTitle(book2.getTitle())
                        .setAuthor(book2.getAuthor())
                        .setDescription(book2.getDescription())
                        .build();
                responseObserver.onNext(response);
            } else {
                responseObserver.onError(new IllegalArgumentException("Book not found"));
            }
            responseObserver.onCompleted();
        }

        @Override
        public void getBookReviews(GetBookReviewsRequest request, StreamObserver<BookReview> responseObserver) {
            if ("1".equals(request.getBookId())) {
                responseObserver.onNext(review1);
                responseObserver.onNext(review2);
            } else if ("2".equals(request.getBookId())) {
                responseObserver.onNext(review1);
            } else {
                responseObserver.onError(new IllegalArgumentException("Book not found"));
            }
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<SubmitBookReviewRequest> submitBookReview(StreamObserver<SubmitBookReviewResponse> responseObserver) {
            return new StreamObserver<>() {
                @Override
                public void onNext(SubmitBookReviewRequest request) {
                    // Process the submitted review (for simplicity, just print it)
                    System.out.println("Received review for Book " + request.getBookId() +
                            " from " + request.getReviewerName() +
                            ": " + request.getReviewText());
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("Error in submitting book review: " + t.getMessage());
                }

                @Override
                public void onCompleted() {
                    SubmitBookReviewResponse response = SubmitBookReviewResponse.newBuilder()
                            .setMessage("Review submitted successfully")
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            };
        }
    }
