package ru.company.news.api.integrationTest;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.company.news.api.dto.comment.request.CreateCommentRequest;
import ru.company.news.api.dto.comment.request.UpdateCommentRequest;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.repository.comment.CommentRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Comment controller integration test")
public class CommentControllerTestIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private Gson gson;

    private List<Comment> commentList = new ArrayList<>();

    @BeforeEach
    void dataInitialization() {
        commentList = commentRepository.findAll();
    }

    @Test
    @DisplayName("Valid create comment test")
    void validCreateCommentTest() throws Exception {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("Клево", commentList.get(0).getUsername(),
                UUID.fromString("625a8ab1-ddd9-4736-878f-093225b42f0b"));

        mockMvc.perform(MockMvcRequestBuilders.post("/comment")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(createCommentRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.matchesRegex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")))
                .andExpect(jsonPath("$.date", Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andExpect(jsonPath("$.text", Matchers.is("Клево")))
                .andExpect(jsonPath("$.username", Matchers.is(commentList.get(0).getUsername())))
                .andExpect(jsonPath("$.news").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Valid read comment test")
    void validReadCommentTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/comment/details/{id}", commentList.get(1).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(commentList.get(1).getId().toString())))
                .andExpect(jsonPath("$.date", Matchers.is(commentList.get(1).getDate())))
                .andExpect(jsonPath("$.text", Matchers.is(commentList.get(1).getText())))
                .andExpect(jsonPath("$.username", Matchers.is(commentList.get(1).getUsername())))
                .andExpect(jsonPath("$.news").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Valid update comment test")
    void validUpdateCommentTest() throws Exception {
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("Супер", "Max",
                commentList.get(2).getNews().getId());

        mockMvc.perform(MockMvcRequestBuilders.put("/comment/{id}", commentList.get(2).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(updateCommentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(commentList.get(2).getId().toString())))
                .andExpect(jsonPath("$.date", Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andExpect(jsonPath("$.text", Matchers.is("Супер")))
                .andExpect(jsonPath("$.username", Matchers.is("Max")))
                .andExpect(jsonPath("$.news").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Valid delete comment test")
    void validDeleteCommentTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/{id}", commentList.get(3).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(gson.toJson(commentList.get(3).getId())))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("Invalid create comment test")
    void invalidCreateCommentTest() throws Exception {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("Клево", commentList.get(0).getUsername(),
                UUID.randomUUID());

        mockMvc.perform(MockMvcRequestBuilders.post("/comment")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(createCommentRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.localDate",
                        Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Invalid read comment test")
    void invalidReadCommentTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/comment/details/{id}", UUID.randomUUID())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.localDate",
                        Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Invalid update comment test")
    void invalidUpdateCommentTest() throws Exception {
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("Супер", "Max",
                commentList.get(2).getNews().getId());

        mockMvc.perform(MockMvcRequestBuilders.put("/comment/{id}", UUID.randomUUID())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(updateCommentRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.localDate",
                        Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Invalid delete comment test")
    void invalidDeleteCommentTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/{id}", UUID.randomUUID())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.localDate",
                        Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Read comment by cache test")
    void readCommentByCacheTest() throws Exception {

        // First access to the controller, saving to the cache.
        mockMvc.perform(MockMvcRequestBuilders.get("/comment/details/{id}", commentList.get(1).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.text", Matchers.is(commentList.get(1).getText())))
                .andDo(MockMvcResultHandlers.print());

        Comment comment = commentRepository.findById(commentList.get(1).getId()).get();
        comment.setText(" ");
        commentRepository.save(comment);

        // Second access to the controller, the data has been changed, but the old ones stored in the cache are returned.
        mockMvc.perform(MockMvcRequestBuilders.get("/comment/details/{id}", commentList.get(1).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.text", Matchers.is(commentList.get(1).getText())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Update comment by cache test")
    void updateCommentByCacheTest() throws Exception {
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("Супер", "Max",
                commentList.get(2).getNews().getId());

        // First access to the controller, saving to the cache.
        mockMvc.perform(MockMvcRequestBuilders.get("/comment/details/{id}", commentList.get(2).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.text", Matchers.is(commentList.get(2).getText())))
                .andDo(MockMvcResultHandlers.print());

        // Refresh data, clear cache
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/{id}", commentList.get(2).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(updateCommentRequest)))
                .andDo(MockMvcResultHandlers.print());

        // Second controller call, no saved cache, database call.
        mockMvc.perform(MockMvcRequestBuilders.get("/comment/details/{id}", commentList.get(2).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.text", Matchers.is("Супер")))
                .andDo(MockMvcResultHandlers.print());
    }
}
