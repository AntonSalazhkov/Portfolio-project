package ru.company.news.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.company.news.api.dto.news.request.CreateNewsRequest;
import ru.company.news.api.dto.news.request.UpdateNewsRequest;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.entity.news.News;
import ru.company.news.api.repository.comment.CommentRepository;
import ru.company.news.api.service.news.NewsApiService;
import ru.company.news.api.repository.news.NewsRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("News service test")
@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NewsRepository newsRepository;
    @InjectMocks
    private NewsApiService newsApiService;

    private News news1;
    private News news2;
    private News news3;
    private News news4;
    private UUID uuid1;
    List<News> newsList = new ArrayList<>();

    @BeforeEach
    void dataInitialization() {
        uuid1 = UUID.randomUUID();

        news1 = new News(null, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                "Касаткина выиграла теннисный турнир", "Теперь на ее счету пять титулов на турнирах WTA.");
        news2 = new News(uuid1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                "Касаткина выиграла", "Теперь на ее счету пять титулов на турнирах WTA.");
        news3 = new News(uuid1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                "Касаткина выиграла теннисный турнир", "Теперь на ее счету пять титулов на турнирах WTA.");
        news4 = new News(uuid1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                "Касаткина выиграла теннисный турнир", "Теперь на ее счету пять титулов на турнирах WTA.");
        news3.getComments().add(new Comment(UUID.fromString("5f3319c3-bacf-446b-bb49-6596158807cb"),
                "09-08-2022 17:34", "Хорошая новинка", "Anton", news3));
        news4.getComments().add(new Comment(UUID.fromString("5f3319c3-bacf-446b-bb49-6596158807cb"),
                "09-08-2022 17:34", "Хорошая новинка", "Anton", news3));
        newsList.add(news3);
    }

    @Test
    @DisplayName("Get news test")
    void getNewsTest() {
        Pageable pageable = PageRequest.of(0, 9, Sort.by("date").ascending());
        Page<News> newsPage = new PageImpl<>(newsList);

        when(newsRepository.findAll(pageable)).thenReturn(newsPage);

        assertEquals(newsList, newsApiService.getNews(pageable));
    }

    @Test
    @DisplayName("Get news by parameters test")
    void getNewsByParametersTest() {
        Pageable pageable = PageRequest.of(0, 9, Sort.by("date").ascending());
        String userInputDate = "2022";
        String userInputTitle = "кино";
        String userInputText = "новинка";

        when(newsRepository.getByDateContainingAndTitleContainingIgnoreCaseAndTextContainingIgnoreCase(
                userInputDate, userInputTitle, userInputText, pageable)).thenReturn(newsList);

        assertEquals(newsList, newsApiService.getNews(userInputDate, userInputTitle, userInputText, pageable));
    }

    @Test
    @DisplayName("Valid read news test")
    void validReadNewsTest() {
        Pageable pageable = PageRequest.of(0, 9, Sort.by("date").ascending());
        List<Comment> commentsByPage = Collections.singletonList(news4.getComments().get(0));

        when(newsRepository.findById(uuid1)).thenReturn(Optional.ofNullable(news3));
        when(commentRepository.getByNews(news4, pageable)).thenReturn(commentsByPage);

        Assertions.assertEquals(news3, newsApiService.readNews(uuid1, pageable));
    }

    @Test
    @DisplayName("invalid read news test")
    void invalidReadNewsTest() {
        Pageable pageable = PageRequest.of(0, 9, Sort.by("date").ascending());

        assertThrows(EntityNotFoundException.class, () -> newsApiService.readNews(uuid1, pageable));
    }

    @Test
    @DisplayName("Create news test")
    void createNewsTest() {
        CreateNewsRequest createNewsRequest = new CreateNewsRequest("Касаткина выиграла теннисный турнир",
                "Теперь на ее счету пять титулов на турнирах WTA.");

        when(newsRepository.save(news1)).thenReturn(news1);

        Assertions.assertEquals(news1, newsApiService.createNews(createNewsRequest));
    }

    @Test
    @DisplayName("Valid update news test")
    void validUpdateNewsTest() {
        UpdateNewsRequest updateNewsRequest = new UpdateNewsRequest(uuid1, "Касаткина выиграла",
                "Теперь на ее счету пять титулов на турнирах WTA.");

        when(newsRepository.findById(updateNewsRequest.getId())).thenReturn(Optional.ofNullable(news2));
        when(newsRepository.save(news2)).thenReturn(news2);

        Assertions.assertEquals(news2, newsApiService.updateNews(updateNewsRequest));
    }

    @Test
    @DisplayName("Invalid update news test")
    void invalidUpdateNewsTest() {
        UpdateNewsRequest updateNewsRequest = new UpdateNewsRequest(uuid1, "Касаткина выиграла",
                "Теперь на ее счету пять титулов на турнирах WTA.");

        assertThrows(EntityNotFoundException.class, () -> newsApiService.updateNews(updateNewsRequest));
    }

    @Test
    @DisplayName("Valid delete news test")
    void validDeleteNewsTest() {
        when(newsRepository.findById(uuid1)).thenReturn(Optional.ofNullable(news1));

        assertEquals(uuid1, newsApiService.deleteNews(uuid1));
    }

    @Test
    @DisplayName("Invalid delete news test")
    void invalidDeleteNewsTest() {
        assertThrows(EntityNotFoundException.class, () -> newsApiService.deleteNews(uuid1));
    }
}
