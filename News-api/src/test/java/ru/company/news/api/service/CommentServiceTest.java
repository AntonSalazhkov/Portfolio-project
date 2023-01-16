package ru.company.news.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.company.news.api.dto.comment.request.CreateCommentRequest;
import ru.company.news.api.dto.comment.request.UpdateCommentRequest;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.entity.news.News;
import ru.company.news.api.repository.comment.CommentRepository;
import ru.company.news.api.service.comment.CommentApiService;
import ru.company.news.api.repository.news.NewsRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Comment service test")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NewsRepository newsRepository;
    @InjectMocks
    private CommentApiService commentApiService;


    private Comment comment1;
    private Comment comment2;
    private News news;
    private UUID uuid1;
    private UUID uuid2;

    @BeforeEach
    void dataInitialization() {
        uuid1 = UUID.randomUUID();
        uuid2 = UUID.randomUUID();

        news = new News(uuid2, "07-08-2022 17:24", "Касаткина выиграла теннисный турнир",
                "Теперь на ее счету пять титулов на турнирах WTA.");
        comment1 = new Comment(null, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                "Ожидаемо", "Anton", news);
        comment2 = new Comment(uuid2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                "Ожидаемо", "Anton", news);
    }

    @Test
    @DisplayName("Valid add product test")
    void validCreateCommentTest() {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("Ожидаемо", "Anton", uuid2);

        when(commentRepository.save(comment1)).thenReturn(comment1);
        when(newsRepository.findById(news.getId())).thenReturn(Optional.ofNullable(news));

        assertEquals(comment1, commentApiService.createComment(createCommentRequest));
    }

    @Test
    @DisplayName("Invalid add product test")
    void invalidCreateCommentTest() {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("Ожидаемо", "Anton", uuid1);

        assertThrows(EntityNotFoundException.class, () -> commentApiService.createComment(createCommentRequest));
    }

    @Test
    @DisplayName("Valid read comment test")
    void validReadCommentTest() {
        when(commentRepository.findById(uuid1)).thenReturn(Optional.ofNullable(comment1));

        assertEquals(comment1, commentApiService.readComment(uuid1));
    }

    @Test
    @DisplayName("Invalid read comment test")
    void invalidReadCommentTest() {
        assertThrows(EntityNotFoundException.class, () -> commentApiService.readComment(uuid2));
    }

    @Test
    @DisplayName("Valid update comment test")
    void validUpdateCommentTest() {
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("Ожидаемо", "Anton", uuid1);

        when(commentRepository.save(comment2)).thenReturn(comment2);
        when(commentRepository.findById(uuid2)).thenReturn(Optional.ofNullable(comment2));

        assertEquals(comment2, commentApiService.updateComment(uuid2, updateCommentRequest));
    }

    @Test
    @DisplayName("Invalid update comment test")
    void invalidUpdateCommentTest() {
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("Ожидаемо", "Anton", uuid1);

        assertThrows(EntityNotFoundException.class, () -> commentApiService.updateComment(uuid1, updateCommentRequest));
    }

    @Test
    @DisplayName("Valid delete comment test")
    void validDeleteCommentTest() {
        when(commentRepository.findById(uuid1)).thenReturn(Optional.ofNullable(comment1));

        assertEquals(uuid1, commentApiService.deleteComment(uuid1));
    }

    @Test
    @DisplayName("Invalid delete comment test")
    void invalidDeleteCommentTest() {
        assertThrows(EntityNotFoundException.class, () -> commentApiService.deleteComment(uuid1));
    }
}
