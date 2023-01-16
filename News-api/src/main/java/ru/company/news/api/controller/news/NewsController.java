package ru.company.news.api.controller.news;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.company.news.api.config.ApiPageable;
import ru.company.news.api.dto.news.request.CreateNewsRequest;
import ru.company.news.api.dto.news.request.UpdateNewsRequest;
import ru.company.news.api.entity.View;
import ru.company.news.api.entity.news.News;
import ru.company.news.api.service.news.NewsService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * The class of controllers of entities - news.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * Controller for receiving the list of news, no comments.
     *
     * @param pageable page number being viewed.
     *                 May be missing.
     *                 Default: page - 0, number of entities per page - 10, sorting of entities - by date.
     * @return list of entities - news, no comments.
     */
    @GetMapping
    @JsonView(View.Public.class)
    @ApiOperation("Controller for receiving the list of news, no comments.")
    @ApiPageable
    public List<News> getNews(@PageableDefault
                              @SortDefault(sort = "date") Pageable pageable) {
        return newsService.getNews(pageable);
    }

    /**
     * Controller for receiving a list of news corresponding to the specified parameters, no comments.
     *
     * @param userInputDate  User-specified parameter: date.
     *                       Can be empty or have a partial match.
     * @param userInputTitle User-supplied parameter: title.
     *                       May be empty, partial match, or different case.
     * @param userInputText  User-specified parameter: text.
     *                       May be empty, partial match, or different case.
     * @param pageable       page number being viewed.
     *                       May be missing.
     *                       Default: page - 0, number of entities per page - 10, sorting of entities - by date.
     * @return list of entities - news, no comments.
     * @throws MissingServletRequestParameterException if there is no parameter (at least empty): date, title and text.
     */
    @GetMapping("/parameter")
    @JsonView(View.Public.class)
    @ApiOperation("Controller for receiving a list of news corresponding to the specified parameters, no comments.")
    @ApiPageable
    public List<News> getNews(@RequestParam("date") String userInputDate,
                              @RequestParam("title") String userInputTitle,
                              @RequestParam("text") String userInputText,
                              @PageableDefault
                              @SortDefault(sort = "date") Pageable pageable) {
        return newsService.getNews(userInputDate, userInputTitle, userInputText, pageable);
    }

    /**
     * New news creation processing controller.
     *
     * @param createNewsRequest request to create a new news item.
     * @return entity - news, no comment.
     * @throws MethodArgumentNotValidException if the fields in the request are incorrectly filled.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(View.Public.class)
    @ApiOperation("New news creation processing controller.")
    public News createNews(@RequestBody @Valid CreateNewsRequest createNewsRequest) {
        return newsService.createNews(createNewsRequest);
    }

    /**
     * Processing controller for reading the details of one news item, with comments.
     *
     * @param id       UUID identifier of the entity - news.
     * @param pageable the number of the comment page being viewed.
     *                 May be missing.
     *                 Default: page - 0, number of entities per page - 10, sorting of entities - by date.
     * @return entity - news, with comment.
     * @throws MethodArgumentTypeMismatchException if the news ID is set incorrectly.
     * @throws EntityNotFoundException             if no news was found for the given identifier.
     */
    @GetMapping("/details/{id}")
    @ApiOperation("Processing controller for reading the details of one news item, with comments.")
    @ApiPageable
    public News readNews(@PathVariable UUID id,
                         @PageableDefault
                         @SortDefault(sort = "date") Pageable pageable) {
        return newsService.readNews(id, pageable);
    }

    /**
     * News update processing controller.
     *
     * @param updateNewsRequest request for news update.
     * @return entity - news, no comment.
     * @throws MethodArgumentNotValidException if the fields in the request are incorrectly filled.
     * @throws InvalidFormatException          if the news ID field in the request is filled incorrectly.
     * @throws EntityNotFoundException         if no news was found by the given identifier in the request.
     */
    @PutMapping
    @JsonView(View.Public.class)
    @ApiOperation("News update processing controller.")
    public News updateNews(@RequestBody @Valid UpdateNewsRequest updateNewsRequest) {
        return newsService.updateNews(updateNewsRequest);
    }

    /**
     * News deletion processing controller.
     *
     * @param id UUID identifier of the entity - news.
     * @return UUID identifier of the entity - news.
     * @throws MethodArgumentTypeMismatchException if the news ID is set incorrectly.
     * @throws EntityNotFoundException             if no news was found for the given identifier.
     */
    @DeleteMapping("/{id}")
    @ApiOperation("News deletion processing controller.")
    public UUID deleteNews(@PathVariable UUID id) {
        return newsService.deleteNews(id);
    }
}
