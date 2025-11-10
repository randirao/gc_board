package gc.board.comment.service;

import gc.board.comment.entity.CommentPath;
import gc.board.comment.entity.CommentV2;
import gc.board.comment.repository.CommentRepositoryV2;
import gc.board.comment.service.request.CommentCreateRequestV2;
import gc.board.comment.service.response.CommentResponse;
import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentServiceV2 {
    private final Snowflake snowflake = new Snowflake();
    private final CommentRepositoryV2 commentRepository;

    @Transactional
    public CommentResponse create(CommentCreateRequestV2 request) {
        CommentV2 parent = findParent(request);
        CommentPath parentCommentPath = parent == null ? CommentPath.create("") : parent.getCommentPath();

        String descendantsTopPath = commentRepository
                .findDecendantTopPath(request.getArticleId(), parentCommentPath.getPath())
                .orElse(null);

        CommentV2 comment = commentRepository.save(
                CommentV2.create(
                        snowflake.nextId(),
                        request.getContent(),
                        request.getArticleId(),
                        request.getWriterId(),
                        parentCommentPath.createChildCommentPath(descendantsTopPath),
                        false
                )
        );
        return CommentResponse.from(comment);
    }

    public CommentResponse read(Long commentId) {
        return CommentResponse.from(commentRepository.findById(commentId).orElseThrow());
    }

    @Transactional
    public void delete(Long commentId) {
        CommentV2 comment = commentRepository.findById(commentId).orElseThrow();

        if (hasChildren(comment)) {
            comment.delete();
            return;
        }

        deleteRecursively(comment);
    }

    @Transactional
    public CommentV2 findParent(CommentCreateRequestV2 request) {
        String parentPath = request.getParentPath();
        if(parentPath == null) {
            return null;
        }
        return commentRepository.findByPath(parentPath)
                .filter(not(CommentV2::getDeleted))
                .orElseThrow();
    }

    private boolean hasChildren(CommentV2 comment) {
        return commentRepository
                .findDecendantTopPath(comment.getArticleId(), comment.getCommentPath().getPath())
                .isPresent();
    }

    private void deleteRecursively(CommentV2 comment) {
        commentRepository.delete(comment);

        if (!comment.isRoot()) {
            commentRepository.findByPath(comment.getCommentPath().getParentPath())
                    .filter(CommentV2::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::deleteRecursively);
        }
    }
}
