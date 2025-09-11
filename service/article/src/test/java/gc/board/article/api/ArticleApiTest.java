package gc.board.article.api;

import gc.board.article.service.response.ArticlePageResponse;
import gc.board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ArticleApiTest {
    RestClient client = RestClient.create("http://localhost:9000");
    private Long createdId = 221843338205904896L;

    //create
    private ArticleResponse create(ArticleCreateRequest articleCreateRequest) {
        return client.post()
                .uri("/v1/articles")
                .body(articleCreateRequest)
                .retrieve()
                .body(ArticleResponse.class);
    }

    //read
    private ArticleResponse read(Long articleId) {
        return client.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    //update
    private void update(Long articleId, ArticleUpdateRequest request) {
        client.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    //delete
    private void delete(Long articleId) {
        client.delete()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .toBodilessEntity();
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }

    @Test
    void createTest() {
        ArticleCreateRequest request = new ArticleCreateRequest("hi", "my content", 1L, 1L);
        ArticleResponse response = create(request);
        System.out.println(response);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(createdId);
        System.out.println(response);
    }

    @Test
    void updateTest() {
        update(createdId, new ArticleUpdateRequest("hi 2", "my content 2"));
        ArticleResponse updated = read(createdId);
        System.out.println(updated);
    }

    @Test
    void deleteTest() {
        delete(createdId);
    }

    @Test
    void readAllTest() {
        ArticlePageResponse response = client.get()
                .uri("/v1/articles?boardId=1&pageSize=30&page=1")
                .retrieve()
                .body(ArticlePageResponse.class);
        
        System.out.println("Article count: " + response.getArticleCount());
        
        response.getArticles().forEach(article -> {
            System.out.println("Article ID: " + article.getArticleId());
        });
    }
}