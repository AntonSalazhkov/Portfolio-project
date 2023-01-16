package ru.company.news.api.service.comment;

import ru.company.news.api.dto.comment.request.CreateCommentRequest;
import ru.company.news.api.dto.comment.request.UpdateCommentRequest;
import ru.company.news.api.entity.comment.Comment;

import java.util.UUID;

/**
 * Comment processing service.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
public interface CommentService {

    /**
     * Method for adding a new comment.
     *
     * @param createCommentRequest Request to add a comment.
     * @return entity - comment.
     */
    Comment createComment(CreateCommentRequest createCommentRequest);

    /**
     * Method for reading the details of a single comment.
     *
     * @param id UUID identifier of the entity - comment.
     * @return entity - comment.
     */
    Comment readComment(UUID id);

    /**
     * Comment update method.
     *
     * @param id                   UUID identifier of the entity - comment.
     * @param updateCommentRequest comment update request.
     * @return entity - comment.
     */
    Comment updateComment(UUID id, UpdateCommentRequest updateCommentRequest);

    /**
     * Method for deleting a comment.
     *
     * @param id UUID identifier of the entity - comment.
     * @return UUID identifier of the entity - comment.
     */
    UUID deleteComment(UUID id);
}
