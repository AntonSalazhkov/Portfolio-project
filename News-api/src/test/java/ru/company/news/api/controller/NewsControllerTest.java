package ru.company.news.api.controller;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.company.news.api.controller.news.NewsController;
import ru.company.news.api.dto.news.request.CreateNewsRequest;
import ru.company.news.api.dto.news.request.UpdateNewsRequest;
import ru.company.news.api.entity.news.News;
import ru.company.news.api.parameterResolver.InvalidNewsParameterResolver;
import ru.company.news.api.parameterResolver.ValidNewsParameterResolver;
import ru.company.news.api.service.news.NewsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("News controller test")
@WebMvcTest(NewsController.class)
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NewsService newsService;
    @Autowired
    private Gson gson;

    @Nested
    @ExtendWith(MockitoExtension.class)
    @ExtendWith(ValidNewsParameterResolver.class)
    public class ValidData {

        @RepeatedTest(5)
        @DisplayName("Get news test")
        void getNewsTest(News news) throws Exception {
            List<News> newsList = new ArrayList<>();
            newsList.add(news);

            Pageable pageable = PageRequest.of(0, 9, Sort.by("date").ascending());

            when(newsService.getNews(pageable)).thenReturn(newsList);

            mockMvc.perform(MockMvcRequestBuilders.get("/news")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .param("page", "0")
                            .param("size", "9")
                            .param("sort", "date"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.[*].id", Matchers.contains(news.getId().toString())))
                    .andExpect(jsonPath("$.[*].date", Matchers.contains(news.getDate())))
                    .andExpect(jsonPath("$.[*].title", Matchers.contains(news.getTitle())))
                    .andExpect(jsonPath("$.[*].text", Matchers.contains(news.getText())))
                    .andExpect(jsonPath("$.[*].comments").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Get news by parameters test")
        void getNewsByParametersTest(News news) throws Exception {
            List<News> newsList = new ArrayList<>();
            newsList.add(news);
            String userInputDate = news.getDate();
            String userInputTitle = news.getTitle();
            String userInputText = news.getText();
            Pageable pageable = PageRequest.of(0, 9, Sort.by("date").ascending());

            when(newsService.getNews(userInputDate, userInputTitle, userInputText, pageable)).thenReturn(newsList);

            mockMvc.perform(MockMvcRequestBuilders.get("/news/parameter")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .param("date", userInputDate)
                            .param("title", userInputTitle)
                            .param("text", userInputText)
                            .param("page", "0")
                            .param("size", "9")
                            .param("sort", "date"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.[*].id", Matchers.contains(news.getId().toString())))
                    .andExpect(jsonPath("$.[*].date", Matchers.contains(news.getDate())))
                    .andExpect(jsonPath("$.[*].title", Matchers.contains(news.getTitle())))
                    .andExpect(jsonPath("$.[*].text", Matchers.contains(news.getText())))
                    .andExpect(jsonPath("$.[*].comments").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Create news test")
        void createNewsTest(News news) throws Exception {
            CreateNewsRequest createNewsRequest = new CreateNewsRequest(news.getTitle(), news.getText());

            when(newsService.createNews(createNewsRequest)).thenReturn(news);

            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(createNewsRequest)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", Matchers.matchesRegex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")))
                    .andExpect(jsonPath("$.date", Matchers.is(news.getDate())))
                    .andExpect(jsonPath("$.title", Matchers.is(news.getTitle())))
                    .andExpect(jsonPath("$.text", Matchers.is(news.getText())))
                    .andExpect(jsonPath("$.comments").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Read news test")
        void readNewsTest(News news) throws Exception {
            Pageable pageable = PageRequest.of(0, 9, Sort.by("date").ascending());

            when(newsService.readNews(news.getId(), pageable)).thenReturn(news);

            mockMvc.perform(MockMvcRequestBuilders.get("/news/details/{id}", news.getId())
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .param("page", "0")
                            .param("size", "9")
                            .param("sort", "date"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", Matchers.is(news.getId().toString())))
                    .andExpect(jsonPath("$.date", Matchers.is(news.getDate())))
                    .andExpect(jsonPath("$.title", Matchers.is(news.getTitle())))
                    .andExpect(jsonPath("$.text", Matchers.is(news.getText())))
                    .andExpect(jsonPath("$.comments[:1].id", Matchers.contains(news.getComments().get(0).getId().toString())))
                    .andExpect(jsonPath("$.comments[:1].date", Matchers.contains(news.getComments().get(0).getDate())))
                    .andExpect(jsonPath("$.comments[:1].text", Matchers.contains(news.getComments().get(0).getText())))
                    .andExpect(jsonPath("$.comments[:1].username", Matchers.contains(news.getComments().get(0).getUsername())))
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Update news test")
        void updateNewsTest(News news) throws Exception {
            UpdateNewsRequest updateNewsRequest = new UpdateNewsRequest(news.getId(), news.getTitle(), news.getText());

            when(newsService.updateNews(updateNewsRequest)).thenReturn(news);

            mockMvc.perform(MockMvcRequestBuilders.put("/news")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(updateNewsRequest)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", Matchers.is(news.getId().toString())))
                    .andExpect(jsonPath("$.date", Matchers.is(news.getDate())))
                    .andExpect(jsonPath("$.title", Matchers.is(news.getTitle())))
                    .andExpect(jsonPath("$.text", Matchers.is(news.getText())))
                    .andExpect(jsonPath("$.comments").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("Delete news test")
        void deleteNewsTest() throws Exception {
            UUID id = UUID.randomUUID();

            when(newsService.deleteNews(id)).thenReturn(id);

            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.content().json(gson.toJson(id)))
                    .andDo(MockMvcResultHandlers.print());
        }
    }


    @Nested
    @ExtendWith(MockitoExtension.class)
    @ExtendWith(InvalidNewsParameterResolver.class)
    public class InvalidData {

        @Test
        @DisplayName("Get news test")
        void getNewsTest(News news) throws Exception {
            List<News> newsList = new ArrayList<>();
            newsList.add(news);

            Pageable pageable = PageRequest.of(0, 9, Sort.by("date").ascending());

            when(newsService.getNews(pageable)).thenReturn(newsList);

            // .param("page") = 1
            mockMvc.perform(MockMvcRequestBuilders.get("/news")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .param("page", "1")
                            .param("size", "9")
                            .param("sort", "date"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.[*].id").doesNotExist())
                    .andExpect(jsonPath("$.[*].date").doesNotExist())
                    .andExpect(jsonPath("$.[*].title").doesNotExist())
                    .andExpect(jsonPath("$.[*].text").doesNotExist())
                    .andExpect(jsonPath("$.[*].comments").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("Get news by parameters test")
        void getNewsByParametersTest() throws Exception {

            // no  .param("text")
            mockMvc.perform(MockMvcRequestBuilders.get("/news/parameter")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .param("date", "111")
                            .param("title", "222")
                            .param("page", "0")
                            .param("size", "9")
                            .param("sort", "date"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.localDate",
                            Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Create news test")
        void createNewsTest(News news) throws Exception {
            CreateNewsRequest createNewsRequest = new CreateNewsRequest(news.getTitle(), news.getText());

            when(newsService.createNews(createNewsRequest)).thenReturn(news);

            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(createNewsRequest)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.localDate",
                            Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Read news test")
        void readNewsTest(News news) throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/news/details/{id}", news.getId() + "-123")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .param("page", "0")
                            .param("size", "9")
                            .param("sort", "date"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.localDate",
                            Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                    .andDo(MockMvcResultHandlers.print());
        }

        @RepeatedTest(5)
        @DisplayName("Update news test")
        void updateNewsTest(News news) throws Exception {
            UpdateNewsRequest updateNewsRequest = new UpdateNewsRequest(news.getId(), news.getTitle(), news.getText());

            mockMvc.perform(MockMvcRequestBuilders.put("/news")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(updateNewsRequest)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.localDate",
                            Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("Delete news test")
        void deleteNewsTest() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", "11a908f1-aaff-4d41-a367-")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.localDate",
                            Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                    .andDo(MockMvcResultHandlers.print());
        }
    }
}
