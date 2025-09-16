package gc.board.article.integration;

import gc.board.article.service.response.ArticleResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class ArticleInfiniteScrollIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void infiniteScrollIntegrationTest() {
        Long boardId = 1L;
        Long pageSize = 30L;

        // 첫 번째 페이지 요청 (lastArticleId 없이)
        String firstPageUrl = String.format("/v1/articles/infinite-scroll?boardId=%d&pageSize=%d", boardId, pageSize);

        ResponseEntity<List<ArticleResponse>> firstPageResponse = restTemplate.exchange(
                firstPageUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ArticleResponse>>() {}
        );

        List<ArticleResponse> firstPageArticles = firstPageResponse.getBody();

        if (firstPageArticles != null && !firstPageArticles.isEmpty()) {
            log.info("첫 번째 페이지 Article ID 목록:");
            for (ArticleResponse article : firstPageArticles) {
                log.info("Article ID: {}", article.getArticleId());
            }

            // 커서 추출 (마지막 아티클의 ID)
            Long lastArticleId = firstPageArticles.get(firstPageArticles.size() - 1).getArticleId();

            // 두 번째 페이지 요청 (lastArticleId 포함)
            String secondPageUrl = String.format("/v1/articles/infinite-scroll?boardId=%d&pageSize=%d&lastArticleId=%d",
                    boardId, pageSize, lastArticleId);

            ResponseEntity<List<ArticleResponse>> secondPageResponse = restTemplate.exchange(
                    secondPageUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ArticleResponse>>() {}
            );

            List<ArticleResponse> secondPageArticles = secondPageResponse.getBody();

            if (secondPageArticles != null && !secondPageArticles.isEmpty()) {
                log.info("두 번째 페이지 Article ID 목록:");
                for (ArticleResponse article : secondPageArticles) {
                    log.info("Article ID: {}", article.getArticleId());
                }

                // 연속성 확인
                Long firstArticleIdOfSecondPage = secondPageArticles.get(0).getArticleId();
                boolean isContinuous = lastArticleId > firstArticleIdOfSecondPage;
                log.info("연속성 확인 ({} > {}): {}", lastArticleId, firstArticleIdOfSecondPage, isContinuous);

                // 중복 없음 확인
                boolean hasDuplicates = firstPageArticles.stream()
                        .anyMatch(firstArticle -> secondPageArticles.stream()
                                .anyMatch(secondArticle -> firstArticle.getArticleId().equals(secondArticle.getArticleId())));
                log.info("중복 데이터 없음: {}", !hasDuplicates);
            }
        }
    }
}