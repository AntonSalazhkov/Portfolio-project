package ru.company.news.api.dto.news.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Request to add news.
 * There is validation by fields.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewsRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String text;
}
