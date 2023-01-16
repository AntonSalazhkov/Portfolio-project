package ru.company.news.api.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.entity.news.News;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Entities Test")
public class EntitiesTest {

    @Test
    @DisplayName("Test creation news entities")
    void newsCreationTest() {
        UUID uuid = UUID.randomUUID();
        News news1 = new News();
        News news2 = new News(uuid, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                "Касаткина выиграла теннисный турнир", "Теперь на ее счету пять титулов на турнирах WTA.");

        assertNull(news1.getId());
        assertNull(news1.getDate());
        assertNull(news1.getTitle());
        assertNull(news1.getText());
        assertEquals(new ArrayList<>(), news1.getComments());

        assertEquals(uuid, news2.getId());
        assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), news2.getDate());
        assertEquals("Касаткина выиграла теннисный турнир", news2.getTitle());
        assertEquals("Теперь на ее счету пять титулов на турнирах WTA.", news2.getText());
        assertEquals(new ArrayList<>(), news2.getComments());
    }

    @Test
    @DisplayName("Test creation comment entities")
    void commentCreationTest() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        News news = new News(uuid1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                "Касаткина выиграла теннисный турнир", "Теперь на ее счету пять титулов на турнирах WTA.");
        Comment comment1 = new Comment();
        Comment comment2 = new Comment(uuid2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                "Ожидаемо", "Anton", news);

        assertNull(comment1.getId());
        assertNull(comment1.getDate());
        assertNull(comment1.getText());
        assertNull(comment1.getUsername());
        assertNull(comment1.getNews());

        assertEquals(uuid2, comment2.getId());
        assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), comment2.getDate());
        assertEquals("Ожидаемо", comment2.getText());
        assertEquals("Anton", comment2.getUsername());
        assertEquals(news, comment2.getNews());
    }
}
