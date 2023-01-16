package ru.company.news.api.integrationTest;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.company.news.api.dto.news.request.CreateNewsRequest;
import ru.company.news.api.dto.news.request.UpdateNewsRequest;
import ru.company.news.api.entity.news.News;
import ru.company.news.api.repository.news.NewsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("News controller integration test")
public class NewsControllerTestIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private Gson gson;

    private List<News> newsList = new ArrayList<>();

    @BeforeEach
    void dataInitialization() {
        Pageable pageable = PageRequest.of(0, 11, Sort.by("date").ascending());
        newsList = newsRepository.findAll(pageable).getContent();
    }

    @Test
    @DisplayName("Valid get news test")
    void validGetNewsTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/news")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[:1].id", Matchers.contains(newsList.get(0).getId().toString())))
                .andExpect(jsonPath("$.[:1].date", Matchers.contains(newsList.get(0).getDate())))
                .andExpect(jsonPath("$.[:1].title", Matchers.contains(newsList.get(0).getTitle())))
                .andExpect(jsonPath("$.[:1].text", Matchers.contains(newsList.get(0).getText())))
                .andExpect(jsonPath("$.[:1].comments").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Valid get news by parameters test")
    void validGetNewsByParametersTest() throws Exception {
        String userInputDate = newsList.get(1).getDate();
        String userInputTitle = newsList.get(1).getTitle().toLowerCase();
        String userInputText = newsList.get(1).getText().toUpperCase();

        mockMvc.perform(MockMvcRequestBuilders.get("/news/parameter")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("date", userInputDate)
                        .param("title", userInputTitle)
                        .param("text", userInputText)
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id", Matchers.contains(newsList.get(1).getId().toString())))
                .andExpect(jsonPath("$.[*].date", Matchers.contains(newsList.get(1).getDate())))
                .andExpect(jsonPath("$.[*].title", Matchers.contains(newsList.get(1).getTitle())))
                .andExpect(jsonPath("$.[*].text", Matchers.contains(newsList.get(1).getText())))
                .andExpect(jsonPath("$.[*].comments").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Valid create news test")
    void validCreateNewsTest() throws Exception {
        CreateNewsRequest createNewsRequest = new CreateNewsRequest("Заголовок новости", "Текст новости");

        mockMvc.perform(MockMvcRequestBuilders.post("/news")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(createNewsRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.matchesRegex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")))
                .andExpect(jsonPath("$.date", Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andExpect(jsonPath("$.title", Matchers.is("Заголовок новости")))
                .andExpect(jsonPath("$.text", Matchers.is("Текст новости")))
                .andExpect(jsonPath("$.comments").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Valid read news test")
    void validReadNewsTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/news/details/{id}", newsList.get(2).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                        .param("size", "4")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(newsList.get(2).getId().toString())))
                .andExpect(jsonPath("$.date", Matchers.is(newsList.get(2).getDate())))
                .andExpect(jsonPath("$.title", Matchers.is(newsList.get(2).getTitle())))
                .andExpect(jsonPath("$.text", Matchers.is(newsList.get(2).getText())))
                .andExpect(jsonPath("$.comments[:1].id", Matchers.contains(newsList.get(2).getComments().get(0).getId().toString())))
                .andExpect(jsonPath("$.comments[:1].date", Matchers.contains(newsList.get(2).getComments().get(0).getDate())))
                .andExpect(jsonPath("$.comments[:1].text", Matchers.contains(newsList.get(2).getComments().get(0).getText())))
                .andExpect(jsonPath("$.comments[:1].username", Matchers.contains(newsList.get(2).getComments().get(0).getUsername())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Valid update news test")
    void validUpdateNewsTest() throws Exception {
        UpdateNewsRequest updateNewsRequest = new UpdateNewsRequest(newsList.get(2).getId(),
                "Новый заголовок новости", "Новый текст новости");

        mockMvc.perform(MockMvcRequestBuilders.put("/news")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(updateNewsRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(newsList.get(2).getId().toString())))
                .andExpect(jsonPath("$.date", Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andExpect(jsonPath("$.title", Matchers.is("Новый заголовок новости")))
                .andExpect(jsonPath("$.text", Matchers.is("Новый текст новости")))
                .andExpect(jsonPath("$.comments").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Valid delete news test")
    void validDeleteNewsTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", newsList.get(3).getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(gson.toJson(newsList.get(3).getId())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Invalid get news test")
    void invalidGetNewsTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/news1111")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Invalid get news by parameters test")
    void invalidGetNewsByParametersTest() throws Exception {
        String userInputDate = newsList.get(1).getDate();
        String userInputTitle = newsList.get(1).getTitle().toLowerCase();

        // no  .param("text")
        mockMvc.perform(MockMvcRequestBuilders.get("/news/parameter")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("date", userInputDate)
                        .param("title", userInputTitle)
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.localDate",
                        Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Invalid create news test")
    void invalidCreateNewsTest() throws Exception {
        CreateNewsRequest createNewsRequest = new CreateNewsRequest(" ", "Текст новости");

        mockMvc.perform(MockMvcRequestBuilders.post("/news")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(createNewsRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.localDate",
                        Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Invalid read news test")
    void invalidReadNewsTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/news/details/{id}", newsList.get(2).getId() + "-123")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                        .param("size", "4")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.localDate",
                        Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Invalid update news test")
    void invalidUpdateNewsTest() throws Exception {
        UpdateNewsRequest updateNewsRequest = new UpdateNewsRequest(newsList.get(2).getId(),
                "Новый заголовок новости", "");

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
    @DisplayName("Invalid delete news test")
    void invalidDeleteNewsTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", "11a908f1-aaff-4d41-a367-ff91742ab555")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.localDate",
                        Matchers.is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Get news by cache test")
    void getNewsByCacheTest() throws Exception {

        // First access to the controller, saving to the cache.
        mockMvc.perform(MockMvcRequestBuilders.get("/news")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[:1].title", Matchers.contains(newsList.get(0).getTitle())))
                .andDo(MockMvcResultHandlers.print());

        News news = newsRepository.findById(newsList.get(0).getId()).get();
        news.setTitle(" ");
        newsRepository.save(news);

        // Second access to the controller, the data has been changed, but the old ones stored in the cache are returned.
        mockMvc.perform(MockMvcRequestBuilders.get("/news")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[:1].title", Matchers.contains(newsList.get(0).getTitle())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Update news by cache test")
    void updateNewsByCacheTest() throws Exception {
        UpdateNewsRequest updateNewsRequest = new UpdateNewsRequest(newsList.get(0).getId(),
                "Новый заголовок новости", newsList.get(0).getText());

        // First access to the controller, saving to the cache.
        // newsList.get(0)
        mockMvc.perform(MockMvcRequestBuilders.get("/news")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[:1].title", Matchers.contains(newsList.get(0).getTitle())))
                .andDo(MockMvcResultHandlers.print());

        // Refresh data, clear cache
        mockMvc.perform(MockMvcRequestBuilders.put("/news")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(updateNewsRequest)))
                .andDo(MockMvcResultHandlers.print());

        // Second controller call, no saved cache, database call.
        // newsList.get(1), because previous data has been updated and moved up the list.
        mockMvc.perform(MockMvcRequestBuilders.get("/news")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "date"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[:1].title", Matchers.contains(newsList.get(1).getTitle())))
                .andDo(MockMvcResultHandlers.print());
    }
}
