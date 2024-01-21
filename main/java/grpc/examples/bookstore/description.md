### Interview Task: Online Bookstore Service

Imagine you are building an online bookstore service. The service should allow clients to perform basic operations such as listing available books, retrieving book details, and submitting book reviews. You are tasked with designing and implementing a gRPC service for this purpose.

#### Service Definition (protobuf):

```protobuf
syntax = "proto3";

service BookstoreService {
  // Unary RPC: Get a list of available books
  rpc ListBooks(ListBooksRequest) returns (ListBooksResponse);

  // Unary RPC: Get details of a specific book
  rpc GetBookDetails(GetBookDetailsRequest) returns (BookDetailsResponse);

  // Server Streaming RPC: Get book reviews
  rpc GetBookReviews(GetBookReviewsRequest) returns (stream BookReview);

  // Client Streaming RPC: Submit a book review
  rpc SubmitBookReview(stream SubmitBookReviewRequest) returns (SubmitBookReviewResponse);
}

message ListBooksRequest {
  // Any request parameters, if needed
}

message ListBooksResponse {
  repeated Book book = 1;
}

message GetBookDetailsRequest {
  string book_id = 1;
}

message BookDetailsResponse {
  string title = 1;
  string author = 2;
  string description = 3;
}

message GetBookReviewsRequest {
  string book_id = 1;
}

message BookReview {
  string reviewer_name = 1;
  string review_text = 2;
}

message SubmitBookReviewRequest {
  string book_id = 1;
  string reviewer_name = 2;
  string review_text = 3;
}

message SubmitBookReviewResponse {
  string message = 1;
}
```

#### Tasks:

1. `Implementation:`
    - Implement the server-side logic for the `BookstoreService` in a programming language of your choice (e.g., Java, Python).
    - Include sample data for books and reviews.

2. `Client:`
    - Write a gRPC client that interacts with the implemented service.
    - Demonstrate how the client can call each of the RPC methods.

3. `Documentation:`
    - Provide brief documentation explaining how to run the server and client.
    - Include any necessary dependencies or instructions for setting up the environment.

4. `Testing:`
    - Write basic test cases to verify the functionality of the server.
    - Consider testing various scenarios, including listing books, retrieving book details, getting reviews, and submitting reviews.

### Evaluation Criteria:

1. `Correctness:`
    - The server and client should correctly implement the specified gRPC service.
    - The implementation should handle various scenarios and edge cases.

2. `Documentation:`
    - The documentation should be clear and concise, providing instructions on running the server and client.

3. `Coding Style:`
    - Follow good coding practices, including code organization, readability, and comments where necessary.

4. `Testing:`
    - Demonstrate that the implemented service is tested using meaningful test cases.

5. `Bonus:`
    - If time allows, consider adding features like error handling, logging, or authentication.

This task is designed to evaluate your understanding of gRPC concepts, ability to design a service, and implement it in a programming language of your choice.