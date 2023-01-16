package ru.company.news.api.controller.comment;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.company.news.api.dto.comment.request.CreateCommentRequest;
import ru.company.news.api.dto.comment.request.UpdateCommentRequest;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.service.comment.CommentService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.UUID;

/**
 * The class of controllers of entities - comments.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * Controller for processing the creation of a new comment.
     *
     * @param createCommentRequest request to create a new comment.
     * @return entity - comment.
     * @throws MethodArgumentNotValidException if the fields in the request are filled incorrectly.
     * @throws InvalidFormatException          if the news ID field in the request is filled incorrectly.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Controller for processing the creation of a new comment.")
    public Comment createComment(@RequestBody @Valid CreateCommentRequest createCommentRequest) {
        return commentService.createComment(createCommentRequest);
    }

    /**
     * Processing controller for reading the details of a single comment.
     *
     * @param id UUID identifier of the entity - comment.
     * @return entity - comment.
     * @throws MethodArgumentTypeMismatchException if the comment ID is not set correctly.
     * @throws EntityNotFoundException             if no comment is found for the given identifier in the request.
     */
    @GetMapping("/details/{id}")
    @ApiOperation("Processing controller for reading the details of a single comment.")
    public Comment readComment(@PathVariable UUID id) {
        return commentService.readComment(id);
    }

    /**
     * Comment update processing controller.
     *
     * @param id                   UUID identifier of the entity - comment.
     * @param updateCommentRequest request to update the comment.
     * @return entity - comment.
     * @throws MethodArgumentTypeMismatchException if the comment ID is not set correctly.
     * @throws EntityNotFoundException             if no comment or news was found for the given identifier in the request.
     * @throws MethodArgumentNotValidException     if the fields in the request are filled incorrectly.
     * @throws InvalidFormatException              if the news ID field in the request is filled incorrectly.
     */
    @PutMapping("/{id}")
    @ApiOperation("Comment update processing controller.")
    public Comment updateComment(@PathVariable UUID id,
                                 @RequestBody @Valid UpdateCommentRequest updateCommentRequest) {
        return commentService.updateComment(id, updateCommentRequest);
    }

    /**
     * Comment deletion processing controller.
     *
     * @param id UUID identifier of the entity - comment.
     * @return UUID identifier of the entity - comment.
     * @throws MethodArgumentTypeMismatchException if the comment ID is not set correctly.
     * @throws EntityNotFoundException             if no comment is found for the given identifier in the request.
     */
    @DeleteMapping("/{id}")
    @ApiOperation("Comment deletion processing controller.")
    public UUID deleteComment(@PathVariable UUID id) {
        return commentService.deleteComment(id);
    }
}
