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
}