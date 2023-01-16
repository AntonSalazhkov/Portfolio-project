package ru.company.news.api.dto.news.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Request for news update.
 * There is validation by fields.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNewsRequest {

    @NotNull
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;
}
