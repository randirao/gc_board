package gc.board.article.service.response;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ArticlePageResponse {
    private final List<ArticleResponse> articles;
    private final Long articleCount;

    private ArticlePageResponse(List<ArticleResponse> articles, Long articleCount) {
        this.articles = articles;
        this.articleCount = articleCount;
    }

    public static ArticlePageResponse of(List<ArticleResponse> articles, Long articleCount) {
        return new ArticlePageResponse(articles, articleCount);
    }
}