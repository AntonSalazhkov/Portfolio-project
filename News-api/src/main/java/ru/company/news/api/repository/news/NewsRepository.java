package ru.company.news.api.repository.news;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.company.news.api.entity.news.News;

import java.util.List;
import java.util.UUID;

/**
 * Repository for entity "News".
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
public interface NewsRepository extends JpaRepository<News, UUID> {

    /**
     * Method for obtaining a list of news by the received parameters.
     *
     * @param userInputDate  User-specified parameter: date.
     *                       Can be empty or have a partial match.
     * @param userInputTitle User-supplied parameter: title.
     *                       May be empty, partial match, or different case.
     * @param userInputText  User-specified parameter: text.
     *                       May be empty, partial match, or different case.
     * @param pageable       The page number being viewed.
     *                       May be missing.
     * @return list of entities - news.
     */
    List<News> getByDateContainingAndTitleContainingIgnoreCaseAndTextContainingIgnoreCase(
            String userInputDate, String userInputTitle, String userInputText, Pageable pageable);
}
