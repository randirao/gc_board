package gc.board.comment.service;

import gc.board.comment.repository.CommentRepository;
import gc.board.comment.service.request.CommentCreateRequest;
import gc.board.comment.service.response.CommentResponse;
import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final Snowflake snowflake =  new Snowflake();

    //생성
    @Transactional
    public CommentResponse create(CommentCreateRequest request) {

    }

    //읽기

    //삭제
}
