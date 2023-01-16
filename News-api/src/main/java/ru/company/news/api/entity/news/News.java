package ru.company.news.api.entity.news;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.company.news.api.entity.View;
import ru.company.news.api.entity.comment.Comment;

import javax.persistence.*;
import java.util.*;

/**
 * Entity "News".
 * In entity, there is a @OneToMany relation to "Comment".
 * For the "text" field, the initial size has been increased to 2000 characters.
 * The @JsonView annotation determines the visibility of fields during serialization.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(View.Public.class)
    private UUID id;

    @JsonView(View.Public.class)
    private String date;

    @JsonView(View.Public.class)
    private String title;

    @JsonView(View.Public.class)
    @Column(length = 2000)
    private String text;

    @OneToMany(mappedBy = "news", cascade = {CascadeType.ALL})
    private final List<Comment> comments = new ArrayList<>();
}
