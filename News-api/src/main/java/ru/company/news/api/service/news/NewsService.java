package ru.company.news.api.service.news;

import org.springframework.data.domain.Pageable;
import ru.company.news.api.dto.news.request.CreateNewsRequest;
import ru.company.news.api.dto.news.request.UpdateNewsRequest;
import ru.company.news.api.entity.news.News;

import java.util.List;
import java.util.UUID;

/**
 * News processing service.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
public interface NewsService {

    /**
     * Method for getting a list of news, no comments.
     *
     * @param pageable page number being viewed.
     *                 May be missing.
     * @return list of entities - news, no comments.
     */
    List<News> getNews(Pageable pageable);

    /**
     * Method for obtaining a list of news corresponding to the specified parameters, no comments.
     *
     * @param userInputDate  User-specified parameter: date.
     *                       Can be empty or have a partial match.
     * @param userInputTitle User-supplied parameter: title.
     *                       May be empty, partial match, or different case.
     * @param userInputText  User-specified parameter: text.
     *                       May be empty, partial match, or different case.
     * @param pageable       page number being viewed.
     *                       May be missing.
     * @return list of entities - news, no comments.
     */
    List<News> getNews(String userInputDate, String userInputTitle, String userInputText, Pageable pageable);

    /**
     * Method for creating new news.
     *
     * @param createNewsRequest request to create a new news item.
     * @return entity - news, no comment.
     */
    News createNews(CreateNewsRequest createNewsRequest);

    /**
     * Method of reading the details of one news item, with comments.
     *
     * @param id       UUID identifier of the entity - news.
     * @param pageable the number of the comment page being viewed.
     *                 May be missing.
     * @return entity - news, with comments.
     */
    News readNews(UUID id, Pageable pageable);

    /**
     * News update method.
     *
     * @param updateNewsRequest request for news update.
     * @return entity - news, no comment.
     */
    News updateNews(UpdateNewsRequest updateNewsRequest);

    /**
     * News removal method.
     *
     * @param id UUID identifier of the entity - news.
     * @return UUID identifier of the entity - news.
     */
    UUID deleteNews(UUID id);
}
