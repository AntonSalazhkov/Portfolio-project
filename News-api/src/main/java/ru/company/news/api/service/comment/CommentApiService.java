package ru.company.news.api.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.company.news.api.constant.Messages;
import ru.company.news.api.dto.comment.request.CreateCommentRequest;
import ru.company.news.api.dto.comment.request.UpdateCommentRequest;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.entity.news.News;
import ru.company.news.api.repository.comment.CommentRepository;
import ru.company.news.api.repository.news.NewsRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Implementation of the comment processing service.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CommentApiService implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;

    /**
     * Transactional method.
     *
     * @see CommentService#createComment(CreateCommentRequest)
     */
    @Transactional
    @Override
    public Comment createComment(CreateCommentRequest createCommentRequest) {
        News news = newsRepository.findById(createCommentRequest.getIdNews()).orElseThrow(() ->
                new EntityNotFoundException(Messages.ENTITY_NOT_FOUND));
        Comment comment = buildComment(createCommentRequest, news);
        return commentRepository.save(comment);
    }

    /**
     * Cacheable method.
     *
     * @see CommentService#readComment(UUID)
     */
    @Override
    @Cacheable(cacheNames = "comment")
    public Comment readComment(UUID id) {
        return commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Messages.ENTITY_NOT_FOUND));
    }

    /**
     * Transactional method.
     * Removes an entity by key from the cache.
     *
     * @see CommentService#updateComment(UUID, UpdateCommentRequest)
     */
    @Transactional()
    @Override
    @CacheEvict(cacheNames = "comment", key = "#id")
    public Comment updateComment(UUID id, UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Messages.ENTITY_NOT_FOUND));
        Comment newComment = buildUpdateComment(id, updateCommentRequest, comment.getNews());
        return commentRepository.save(newComment);
    }

    /**
     * Removes all entities from the cache.
     *
     * @see CommentService#deleteComment(UUID)
     */
    @Override
    @CacheEvict(cacheNames = "comment", allEntries = true)
    public UUID deleteComment(UUID id) {
        commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Messages.ENTITY_NOT_FOUND));
        commentRepository.deleteById(id);
        return id;
    }

    private Comment buildComment(CreateCommentRequest request, News news) {
        return Comment.builder()
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .text(request.getText())
                .username(request.getUsername())
                .news(news)
                .build();
    }

    private Comment buildUpdateComment(UUID id, UpdateCommentRequest request, News news) {
        return Comment.builder()
                .id(id)
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .text(request.getText())
                .username(request.getUsername())
                .news(news)
                .build();
    }
}
