package ru.company.news.api.controller;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.company.news.api.dto.comment.request.CreateCommentRequest;
import ru.company.news.api.dto.comment.request.UpdateCommentRequest;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.entity.news.News;
import ru.company.news.api.parameterResolver.InvalidCommentParameterResolver;
import ru.company.news.api.parameterResolver.ValidCommentParameterResolver;
import ru.company.news.api.service.comment.CommentService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @Autowired
    private Gson gson;

    @ParameterizedTest
    @MethodSource("provideCreateComment")
    void createCommentTest(Comment comment) throws Exception {
        System.out.println(comment);

            CreateCommentRequest createCommentRequest = new CreateCommentRequest(comment.getText(), comment.getUsername(),
                    comment.getNews().getId());

            when(commentService.createComment(createCommentRequest)).thenReturn(comment);

            mockMvc.perform(post("/comment")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(createCommentRequest)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", Matchers.is(comment.getId().toString())))
                    .andExpect(jsonPath("$.date", Matchers.is(comment.getDate())))
                    .andExpect(jsonPath("$.text", Matchers.is(comment.getText())))
                    .andExpect(jsonPath("$.username", Matchers.is(comment.getUsername())))
                    .andExpect(jsonPath("$.news").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
    }

    private static Stream<Comment> provideCreateComment() throws IOException {
        return Stream.of(
                new Comment(UUID.randomUUID(), "07-08-2022 17:24", "Ожидаемо", "Anton",
                        new News(UUID.randomUUID(), "07-08-2022 17:24", "Касаткина выиграла теннисный турнир",
                                "Теперь на ее счету пять титулов на турнирах WTA.")),
                new Comment(UUID.randomUUID(), "07-09-2022 17:24", "Прикольно", "Maxim",
                        new News(UUID.randomUUID(), "07-08-2022 17:24", "Касаткина проиграла теннисный турнир",
                                "Теперь на ее счету четыре титулов на турнирах WTA.")),
                new Comment(UUID.randomUUID(), "06-08-2022 10:24", "Класс", "Anton",
                        new News(UUID.randomUUID(), "07-08-2022 17:24", "Касаткина была на теннисный турнир",
                                "Теперь на ее счету три титулов на турнирах WTA."))
        );
    }


    @Nested
    @ExtendWith(MockitoExtension.class)
    @ExtendWith(ValidCommentParameterResolver.class)
    public class ValidData {


        @RepeatedTest(5)
        @DisplayName("Read comment test")
        void readCommentTest(Comment comment) throws Exception {
            UUID id = UUID.randomUUID();

            when(commentService.readComment(id)).thenReturn(comment);

            mockMvc.perform(MockMvcRequestBuilders.get("/comment/details/{id}", id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", Matchers.is(comment.getId().toString())))
                    .andExpect(jsonPath("$.date", Matchers.is(comment.getDate())))
                    .andExpect(jsonPath("$.text", Matchers.is(comment.getText())))
                    .andExpect(jsonPath("$.username", Matchers.is(comment.getUsername())))
                    .andExpect(jsonPath("$.news").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Update comment test")
        void updateCommentTest(Comment comment) throws Exception {
            UUID id = UUID.randomUUID();
            UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(comment.getText(), comment.getUsername(),
                    comment.getNews().getId());

            when(commentService.updateComment(id, updateCommentRequest)).thenReturn(comment);

            mockMvc.perform(MockMvcRequestBuilders.put("/comment/{id}", id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(updateCommentRequest)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", Matchers.is(comment.getId().toString())))
                    .andExpect(jsonPath("$.date", Matchers.is(comment.getDate())))
                    .andExpect(jsonPath("$.text", Matchers.is(comment.getText())))
                    .andExpect(jsonPath("$.username", Matchers.is(comment.getUsername())))
                    .andExpect(jsonPath("$.news").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("Delete comment test")
        void deleteCommentTest() throws Exception {
            UUID id = UUID.randomUUID();

            when(commentService.deleteComment(id)).thenReturn(id);

            mockMvc.perform(MockMvcRequestBuilders.delete("/comment/{id}", id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.content().json(gson.toJson(id)))
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @ExtendWith(InvalidCommentParameterResolver.class)
    public class InvalidData {

        @RepeatedTest(5)
        @DisplayName("Create comment test")
        void createCommentTest(Comment comment) throws Exception {
            CreateCommentRequest createCommentRequest = new CreateCommentRequest(comment.getText(), comment.getUsername(),
                    comment.getNews().getId());

            mockMvc.perform(post("/comment")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(createCommentRequest)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.localDate",
                            Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Read comment test")
        void readCommentTest(Comment comment) throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/comment/details/{id}", comment.getId() + "-123")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.localDate",
                            Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Update comment test")
        void updateCommentTest(Comment comment) throws Exception {
            UUID id = UUID.randomUUID();
            UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(comment.getText(), comment.getUsername(),
                    comment.getNews().getId());

            mockMvc.perform(MockMvcRequestBuilders.put("/comment/{id}", id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(updateCommentRequest)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.localDate",
                            Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("Delete comment test")
        void deleteCommentTest() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete("/comment/{id}", "11a908f1-aaff-4d41-a367-")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.localDate",
                            Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                    .andDo(MockMvcResultHandlers.print());
        }
    }
}
