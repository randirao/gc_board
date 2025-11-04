package gc.board.comment.api;

import gc.board.comment.service.response.CommentPageResponse;
import gc.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

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

  @Test
    void readAll(){
      CommentPageResponse response = restClient.get()
              .uri("/v1/comments?articleId=1&page=1&pageSize=10")
              .retrieve()
              .body(CommentPageResponse.class);

      System.out.println("response.getCommentCount() = " + response.getCommentCount());

      for(CommentResponse comment : response.getComments()) {
          if(!comment.getCommentId().equals(comment.getParentCommentId()))
              System.out.print("\t");
        System.out.println("commentId = " + comment.getCommentId());
      }
  }

    @Test
    void readAllInfiniteScroll() {
        final long articleId = 1L;
        final int pageSize = 5;

        // 무한 스크롤 방식의 첫 번째 페이지(response1)
        List<CommentResponse> response1 = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/comments/infinite-scroll")
                        .queryParam("articleId", articleId)
                        .queryParam("pageSize", pageSize)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("first page");
        Assertions.assertNotNull(response1);
        for (CommentResponse comment : response1) {
            if (!comment.getCommentId().equals(comment.getParentCommentId()))
                System.out.print("\t");
            System.out.println("commentId = " + comment.getCommentId());
        }

        Long lastCommentId = response1.getLast().getCommentId();
        Long lastParentId = response1.getLast().getParentCommentId();

        // 무한 스크롤 방식의 두 번째 페이지(response2)
        List<CommentResponse> response2 = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/comments/infinite-scroll")
                        .queryParam("articleId", articleId)
                        .queryParam("pageSize", pageSize)
                        .queryParam("parentId", lastParentId)
                        .queryParam("lastCommentId", lastCommentId)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("second page");
        Assertions.assertNotNull(response2);
        for (CommentResponse comment : response2) {
            if (!comment.getCommentId().equals(comment.getParentCommentId()))
                System.out.print("\t");
            System.out.println("commentId = " + comment.getCommentId());
        }
    }
}
