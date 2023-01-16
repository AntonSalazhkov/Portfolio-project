package ru.company.news.api.repository.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.entity.news.News;

import java.util.List;
import java.util.UUID;

/**
 * Repository for entity "Comment".
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    /**
     * Method for getting a list of comments on a news item.
     *
     * @param news     The desired parameter.
     * @param pageable The page number being viewed.
     *                 May be missing.
     * @return list of entities - comments.
     */
    List<Comment> getByNews(News news, Pageable pageable);
}
