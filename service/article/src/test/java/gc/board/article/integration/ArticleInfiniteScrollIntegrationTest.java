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

        log.info("=".repeat(80));
        log.info("무한스크롤 통합 테스트 시작");
        log.info("=".repeat(80));

        // 첫 번째 페이지 요청 (lastArticleId 없이)
        String firstPageUrl = String.format("/v1/articles/infinite-scroll?boardId=%d&pageSize=%d", boardId, pageSize);
        log.info("첫 번째 페이지 API 호출: {}", firstPageUrl);

        ResponseEntity<List<ArticleResponse>> firstPageResponse = restTemplate.exchange(
                firstPageUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ArticleResponse>>() {}
        );

        List<ArticleResponse> firstPageArticles = firstPageResponse.getBody();
        log.info("첫 번째 페이지 응답 상태: {}", firstPageResponse.getStatusCode());
        log.info("첫 번째 페이지 결과 크기: {}", firstPageArticles != null ? firstPageArticles.size() : 0);

        if (firstPageArticles != null && !firstPageArticles.isEmpty()) {
            log.info("-".repeat(50));
            log.info("첫 번째 페이지 Article ID 목록:");
            for (int i = 0; i < firstPageArticles.size(); i++) {
                ArticleResponse article = firstPageArticles.get(i);
                log.info("[{}] Article ID: {}, Title: {}", i + 1, article.getArticleId(), article.getTitle());
            }

            // 커서 추출 (마지막 아티클의 ID)
            Long lastArticleId = firstPageArticles.get(firstPageArticles.size() - 1).getArticleId();
            log.info("-".repeat(50));
            log.info("첫 번째 페이지 마지막 Article ID (커서): {}", lastArticleId);

            // 두 번째 페이지 요청 (lastArticleId 포함)
            String secondPageUrl = String.format("/v1/articles/infinite-scroll?boardId=%d&pageSize=%d&lastArticleId=%d",
                    boardId, pageSize, lastArticleId);
            log.info("두 번째 페이지 API 호출: {}", secondPageUrl);

            ResponseEntity<List<ArticleResponse>> secondPageResponse = restTemplate.exchange(
                    secondPageUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ArticleResponse>>() {}
            );

            List<ArticleResponse> secondPageArticles = secondPageResponse.getBody();
            log.info("두 번째 페이지 응답 상태: {}", secondPageResponse.getStatusCode());
            log.info("두 번째 페이지 결과 크기: {}", secondPageArticles != null ? secondPageArticles.size() : 0);

            if (secondPageArticles != null && !secondPageArticles.isEmpty()) {
                log.info("-".repeat(50));
                log.info("두 번째 페이지 Article ID 목록:");
                for (int i = 0; i < secondPageArticles.size(); i++) {
                    ArticleResponse article = secondPageArticles.get(i);
                    log.info("[{}] Article ID: {}, Title: {}", i + 1, article.getArticleId(), article.getTitle());
                }

                // 연속성 확인
                Long firstArticleIdOfSecondPage = secondPageArticles.get(0).getArticleId();
                log.info("=".repeat(50));
                log.info("연속성 확인:");
                log.info("첫 페이지 마지막 ID: {}", lastArticleId);
                log.info("두 번째 페이지 첫 ID: {}", firstArticleIdOfSecondPage);

                boolean isContinuous = lastArticleId > firstArticleIdOfSecondPage;
                log.info("연속성 검증 ({} > {}): {}", lastArticleId, firstArticleIdOfSecondPage, isContinuous);

                // 중복 확인
                boolean hasDuplicates = firstPageArticles.stream()
                        .anyMatch(firstArticle -> secondPageArticles.stream()
                                .anyMatch(secondArticle -> firstArticle.getArticleId().equals(secondArticle.getArticleId())));
                log.info("중복 데이터 존재: {}", hasDuplicates);

                log.info("=".repeat(50));
                if (isContinuous && !hasDuplicates) {
                    log.info("✅ 무한스크롤 연속성 및 중복 방지 테스트 성공!");
                } else {
                    log.error("❌ 무한스크롤 테스트 실패!");
                    if (!isContinuous) log.error("   - 연속성 검증 실패");
                    if (hasDuplicates) log.error("   - 중복 데이터 존재");
                }
            } else {
                log.warn("두 번째 페이지 데이터가 비어있습니다.");
            }
        } else {
            log.warn("첫 번째 페이지 데이터가 비어있습니다.");
        }

        log.info("=".repeat(80));
        log.info("무한스크롤 통합 테스트 완료");
        log.info("=".repeat(80));
    }
}