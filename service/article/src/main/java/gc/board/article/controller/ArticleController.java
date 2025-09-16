package gc.board.article.controller;

import gc.board.article.service.ArticleService;
import gc.board.article.service.request.ArticleCreateRequest;
import gc.board.article.service.request.ArticleUpdateRequest;
import gc.board.article.service.response.ArticlePageResponse;
import gc.board.article.service.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    //조회
    @GetMapping("/v1/articles/{articleId}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long articleId) {
        return ResponseEntity.ok(articleService.read(articleId));
    }

    //전체 조회
    @GetMapping("/v1/articles")
    public ArticlePageResponse readAll(
            @RequestParam("boardId") Long boardId,
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize) {
        return articleService.readAll(boardId, page, pageSize);
    }

    //무한스크롤 조회
    @GetMapping("/v1/articles/infinite-scroll")
    public ResponseEntity<List<ArticleResponse>> readAllInfiniteScroll(
            @RequestParam("boardId") Long boardId,
            @RequestParam("pageSize") Long pageSize,
            @RequestParam(value = "lastArticleId", required = false) Long lastArticleId) {
        List<ArticleResponse> articles = articleService.readAllInfiniteScroll(boardId, pageSize, lastArticleId);
        return ResponseEntity.ok(articles);
    }

    //생성
    @PostMapping("/v1/articles")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleCreateRequest articleCreateRequest) {
        return ResponseEntity.ok(articleService.create(articleCreateRequest));
    }

    //수정
    @PutMapping("/v1/articles/{articleId}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest articleUpdateRequest) {
        return ResponseEntity.ok(articleService.update(articleId, articleUpdateRequest));
    }

    //삭제
    @DeleteMapping("/v1/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
        articleService.delete(articleId);
        return ResponseEntity.noContent().build();
    }
}
