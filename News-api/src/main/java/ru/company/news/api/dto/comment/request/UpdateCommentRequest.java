package ru.company.news.api.dto.comment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Request to update a comment.
 * There is validation by fields.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequest {

    @NotBlank
    private String text;
    @NotBlank
    private String username;
    @NotNull
    private UUID idNews;
}
