package ru.company.news.api.service.news;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.company.news.api.constant.Messages;
import ru.company.news.api.dto.news.request.CreateNewsRequest;
import ru.company.news.api.dto.news.request.UpdateNewsRequest;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.entity.news.News;
import ru.company.news.api.repository.comment.CommentRepository;
import ru.company.news.api.repository.news.NewsRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the news processing service.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class NewsApiService implements NewsService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;

    /**
     * Cacheable method.
     *
     * @see NewsService#getNews(Pageable)
     */
    @Override
    @Cacheable(cacheNames = "news")
    public List<News> getNews(Pageable pageable) {
        return newsRepository.findAll(pageable).getContent();
    }

    /**
     * Cacheable method.
     *
     * @see NewsService#getNews(String, String, String, Pageable)
     */
    @Override
    @Cacheable(cacheNames = "news")
    public List<News> getNews(String userInputDate, String userInputTitle, String userInputText, Pageable pageable) {
        return newsRepository.getByDateContainingAndTitleContainingIgnoreCaseAndTextContainingIgnoreCase(
                userInputDate, userInputTitle, userInputText, pageable);
    }

    /**
     * Transactional method.
     * Removes all entities from the cache.
     *
     * @see NewsService#createNews(CreateNewsRequest)
     */
    @Transactional
    @Override
    @CacheEvict(cacheNames = "news", allEntries = true)
    public News createNews(CreateNewsRequest createNewsRequest) {
        News news = buildNews(createNewsRequest);
        return newsRepository.save(news);
    }

    /**
     * @see NewsService#readNews(UUID, Pageable)
     */
    @Override
    public News readNews(UUID id, Pageable pageable) {
        News news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Messages.ENTITY_NOT_FOUND));
        List<Comment> commentsByPage = commentRepository.getByNews(news, pageable);
        news.getComments().retainAll(commentsByPage);
        return news;
    }

    /**
     * Transactional method.
     * Removes all entities from the cache.
     *
     * @see NewsService#updateNews(UpdateNewsRequest)
     */
    @Transactional
    @Override
    @CacheEvict(cacheNames = "news", allEntries = true)
    public News updateNews(UpdateNewsRequest updateNewsRequest) {
        newsRepository.findById(updateNewsRequest.getId()).orElseThrow(() -> new EntityNotFoundException(Messages.ENTITY_NOT_FOUND));
        News news = buildUpdateNews(updateNewsRequest);
        return newsRepository.save(news);
    }

    /**
     * Removes all entities in the cache named "news" and "comment".
     *
     * @see NewsService#deleteNews(UUID)
     */
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "news", allEntries = true),
            @CacheEvict(cacheNames = "comment", allEntries = true)
    })
    public UUID deleteNews(UUID id) {
        newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Messages.ENTITY_NOT_FOUND));
        newsRepository.deleteById(id);
        return id;
    }

    private News buildNews(CreateNewsRequest request) {
        return News.builder()
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .title(request.getTitle())
                .text(request.getText())
                .build();
    }

    private News buildUpdateNews(UpdateNewsRequest request) {
        return News.builder()
                .id(request.getId())
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .title(request.getTitle())
                .text(request.getText())
                .build();
    }
}
