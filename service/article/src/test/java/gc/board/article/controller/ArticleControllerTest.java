package gc.board.article.controller;

import gc.board.article.service.ArticleService;
import gc.board.article.service.response.ArticlePageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Test
    public void readAllTest() throws Exception {
        ArticlePageResponse mockResponse = ArticlePageResponse.of(Collections.emptyList(), 0L);
        when(articleService.readAll(anyLong(), anyLong(), anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/v1/articles")
                .param("boardId", "1")
                .param("page", "1") 
                .param("pageSize", "30"))
                .andExpect(status().isOk());
    }
}