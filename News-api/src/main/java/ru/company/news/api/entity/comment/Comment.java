package ru.company.news.api.entity.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.company.news.api.entity.news.News;

import javax.persistence.*;
import java.util.UUID;

/**
 * Entity "Comment".
 * In entity, there is a @ManyToOne relation to "News".
 * For the "text" field, the initial size has been increased to 2000 characters.
 * The "news" field is annotated with @JsonIgnore to display correctly when serialized.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String date;

    @Column(length = 2000)
    private String text;

    private String username;

    @ManyToOne
    @JoinColumn(name = "news_id")
    @ToString.Exclude
    private News news;

    @JsonIgnore
    public News getNews() {
        return news;
    }
}
