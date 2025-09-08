package gc.board.article.service.response;

import gc.board.article.entity.Article;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleResponse {
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static ArticleResponse from(Article article) {
        ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.articleId = article.getArticleId();
        articleResponse.title = article.getTitle();
        articleResponse.content = article.getContent();
        articleResponse.boardId = article.getBoardId();
        articleResponse.writerId = article.getWriterId();
        articleResponse.createAt = LocalDateTime.now();
        articleResponse.updateAt = LocalDateTime.now();
        return articleResponse;
    }
}
