package gc.board.article.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Article {

    @Id
    private Long articleId;   // PK

    private String title;
    private String content;
    private Long boardId;
    private Long writerId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    // 정적 팩토리 메서드
    public static Article create(Long id, String title, String content, Long boardId, Long writerId) {
        Article article = new Article();
        article.articleId = id;
        article.title = title;
        article.content = content;
        article.boardId = boardId;
        article.writerId = writerId;
        return article;
    }

    // 수정 메서드 (title, content만 수정 가능)
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}