package gc.board.comment.repository;

import gc.board.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  @Query(
          value = "select count(*) from (" +
                  " select comment_id from comment " +
                  " where article_id = :articleId and parent_comment_id = :parentCommentId " +
                  " limit :limit" +
                  ") t",
          nativeQuery = true
  )
  Long countBy(
          @Param("articleId") Long articleId,
          @Param("parentCommentId") Long parentCommentId,
          @Param("limit") Long limit
  );

  @Query(
          value = "select comment.comment_id, comment.content, comment.parent_comment_id, " +
                  "comment.article_id, comment.writer_id, comment.deleted, comment.created_at " +
                  "from (" +
                    "select comment_id from comment where article_id = :articleId " +
                    "order by parent comment_id asc, comment_id asc " +
                    "limit :limit offset :offset " +
                  ") t left join comment on t.comment_id = comment.comment_id ",
          nativeQuery = true
  )

  List<Comment> findAll(
          @Param("articleId") Long articledId,
          @Param("offset") Long offset,
          @Param("limit") Long limit
  );

  Long count(
          @Param("articleId") Long articleId,
          @Param("limit") Long limit
  );

  //무한 스크롤에서 첫번째 페이지 조회

    @Query(
            value = "select comment.comment_id, comment.content, comment.parent_comment_id, " +
            "comment.article_id, comment.writer_id, comment.deleted, comment.created_at " +
            "from comment " +
            "where article_id = :articleId " +
            "order by parent_comment_id asc, comment_id asc " +
            "limit :limit",
        nativeQuery = true
    )
  List<Comment> findAllInfiniteScroll(
        @Param("articleId") Long articleId,
        @Param("limit") Long limit
  );

  //무한 스크롤에서 두번째 페이지 조회

    @Query(
            value = "select comment.comment_id, comment.content, comment.parent_comment_id, " +
            "comment.article_id, comment.writer_id, comment.deleted, comment.created_at " +
            "from comment " +
            "where article_id = :articleId and (" +
                "parent_comment_id > :lastParentCommentId or (" +
                    "parent_comment_id = :lastParentCommentId and comment_id > :lastCommentId" +
                    ")" +
            ")" +
            "order by parent_comment_id asc, comment_id asc " +
            "limit :limit",
        nativeQuery = true
    )
  List<Comment> findAllInfiniteScroll(
        @Param("articleId") Long articleId,
        @Param("limit") Long limit,
        @Param("lastParentCommentId") Long lastParentCommentId,
        @Param("lastCommentId") Long lastCommentId
  );
}
