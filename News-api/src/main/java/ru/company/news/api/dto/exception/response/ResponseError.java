package ru.company.news.api.dto.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Compiled message to be displayed on the page about identified incorrect situations in the program.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

    private String localDate;
    private String message;
    private String error;
}
