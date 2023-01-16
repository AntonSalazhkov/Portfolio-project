package ru.company.news.api.parameterResolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.company.news.api.entity.comment.Comment;
import ru.company.news.api.entity.news.News;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class InvalidCommentParameterResolver implements ParameterResolver {

    public static List<Comment> invalidComment = Arrays.asList(
            new Comment(null, "07-08-2022 17:24", "Ожидаемо", "  ",
                    new News(UUID.randomUUID(), "07-08-2022 17:24", "Касаткина выиграла теннисный турнир",
                            "Теперь на ее счету пять титулов на турнирах WTA.")),
            new Comment(UUID.randomUUID(), "06-08-2022 10:24", "", "Anton",
                    new News(UUID.randomUUID(), "07-08-2022 17:24", "Касаткина была на теннисный турнир",
                            "Теперь на ее счету три титулов на турнирах WTA.")),
            new Comment(UUID.randomUUID(), "06-08-2022 10:24", "Ожидаемо", "", new News())
    );

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Comment.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return invalidComment.get(new Random().nextInt(invalidComment.size()));
    }
}
