package gc.board.comment.controller;

import gc.board.comment.service.CommentService;
import gc.board.comment.service.request.CommentCreateRequest;
import gc.board.comment.service.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;

  //읽기
  @GetMapping("/v1/comments/{commentId}")
  public CommentResponse read(@PathVariable Long commentId) {
    return commentService.read(commentId);
  }

  //생성
  @PostMapping("/v1/comments")
  public CommentResponse create(
          @RequestBody CommentCreateRequest request
  ) {
    return commentService.create(request);
  }

  //삭제
  @DeleteMapping("/v1/comments/{commentId}")
  public void delete(@PathVariable Long commentId) {
    commentService.delete(commentId);
  }
}
