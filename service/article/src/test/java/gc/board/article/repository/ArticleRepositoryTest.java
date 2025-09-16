package gc.board.article.repository;

import gc.board.article.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void findAllTest() {
        Long boardId = 1L;
        Long offset = 1499970L;
        Long limit = 30L;

        List<Article> articles = articleRepository.findAll(boardId, offset, limit);

        log.info("Result list size: {}", articles.size());
        
        for (Article article : articles) {
            log.info("Article ID: {}, Title: {}, Board ID: {}, Writer ID: {}, Created: {}", 
                    article.getArticleId(), 
                    article.getTitle(), 
                    article.getBoardId(), 
                    article.getWriterId(), 
                    article.getCreatedAt());
        }
    }

    @Test
    public void countTest() {
        Long boardId = 1L;
        Long limit = 10000L;

        Long count = articleRepository.count(boardId, limit);

        log.info("Count result: {}", count);
    }

    @Test
    public void findAllInfiniteScrollTest() {
        Long boardId = 1L;
        Long limit = 30L;

        List<Article> firstPage = articleRepository.findAllInfiniteScroll(boardId, limit);

        log.info("첫 번째 페이지 결과 크기: {}", firstPage.size());
        log.info("첫 번째 페이지 Article ID 목록:");
        for (Article article : firstPage) {
            log.info("Article ID: {}", article.getArticleId());
        }

        if (!firstPage.isEmpty()) {
            Long lastArticleId = firstPage.get(firstPage.size() - 1).getArticleId();
            log.info("첫 번째 페이지 마지막 Article ID: {}", lastArticleId);

            List<Article> secondPage = articleRepository.findAllInfiniteScroll(boardId, limit, lastArticleId);

            log.info("두 번째 페이지 결과 크기: {}", secondPage.size());
            log.info("두 번째 페이지 Article ID 목록:");
            for (Article article : secondPage) {
                log.info("Article ID: {}", article.getArticleId());
            }

            if (!secondPage.isEmpty()) {
                Long firstArticleIdOfSecondPage = secondPage.get(0).getArticleId();
                log.info("두 번째 페이지 첫 번째 Article ID: {}", firstArticleIdOfSecondPage);

                boolean isContinuous = lastArticleId > firstArticleIdOfSecondPage;
                log.info("연속성 확인 ({} > {}): {}", lastArticleId, firstArticleIdOfSecondPage, isContinuous);

                if (isContinuous) {
                    log.info("무한스크롤 연속성 테스트 성공!");
                } else {
                    log.error("무한스크롤 연속성 테스트 실패!");
                }
            }
        }
    }
}