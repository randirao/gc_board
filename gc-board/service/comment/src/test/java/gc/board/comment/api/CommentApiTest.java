package gc.board.comment.api;

import gc.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {
  RestClient restClient = RestClient.create("http://localhost:9001");

  @Test
  void createTest() {
    CommentResponse response1 = createComment(
            new CommentCreateRequest(1L, "comment1", null, 1L)
    );
    CommentResponse response2 = createComment(
            new CommentCreateRequest(1L, "comment2", response1.getCommentId(), 1L)
    );
    CommentResponse response3 = createComment(
            new CommentCreateRequest(1L, "comment3", response1.getCommentId(), 1L)
    );

    System.out.println("commentId=%s".formatted(response1.getCommentId()));
    System.out.println("\tcommentId=%s".formatted(response2.getCommentId()));
    System.out.println("\tcommentId=%s".formatted(response3.getCommentId()));
  }

  private CommentResponse createComment(CommentCreateRequest request) {
    return restClient.post()
            .uri("/v1/comments")
            .body(request)
            .retrieve()
            .body(CommentResponse.class);
  }

//  commentId=235996940716695552
//    commentId=235996941253566464
//    commentId=235996941316481024


  @Test
  void readTest() {
    CommentResponse response = restClient.get()
            .uri("/v1/comments/{commentId}", 235996940716695552L)
            .retrieve()
            .body(CommentResponse.class);

    System.out.println("response = " + response);
  }

  //  commentId=235996940716695552
//    commentId=235996941253566464
//    commentId=235996941316481024
  @Test
  void deleteTest() {
    restClient.delete()
            .uri("/v1/comments/{commentId}", 235996941316481024L)
            .retrieve();
  }

  @Getter
  @AllArgsConstructor
  public static class CommentCreateRequest {
    private Long articleId;
    private String content;
    private Long parentCommentId;
    private Long writerId;
  }
}
