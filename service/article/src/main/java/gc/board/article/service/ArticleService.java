package gc.board.article.service;

import gc.board.article.entity.Article;
import gc.board.article.repository.ArticleRepository;
import gc.board.article.service.request.ArticleCreateRequest;
import gc.board.article.service.request.ArticleUpdateRequest;
import gc.board.article.service.response.ArticleResponse;
import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final Snowflake snowflake;
    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest articleCreateRequest) {
        Long id = snowflake.nextId();
        Article article = Article.create(id, articleCreateRequest.getTitle(), articleCreateRequest.getContent(), articleCreateRequest.getBoardId(), articleCreateRequest.getWriterId());
        articleRepository.save(article);
        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article Not Found "+id));
        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse update(Long id, ArticleUpdateRequest articleUpdateRequest) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article Not Found "+id));
        article.update(articleUpdateRequest.getTitle(), articleUpdateRequest.getContent());
        return ArticleResponse.from(article);
    }

    @Transactional
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
