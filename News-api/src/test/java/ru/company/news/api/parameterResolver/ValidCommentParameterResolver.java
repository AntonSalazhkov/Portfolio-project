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

public class ValidCommentParameterResolver implements ParameterResolver {

    public static List<Comment> validComment = Arrays.asList(
            new Comment(UUID.randomUUID(), "07-08-2022 17:24", "Ожидаемо", "Anton",
                    new News(UUID.randomUUID(), "07-08-2022 17:24", "Касаткина выиграла теннисный турнир",
                            "Теперь на ее счету пять титулов на турнирах WTA.")),
            new Comment(UUID.randomUUID(), "07-09-2022 17:24", "Прикольно", "Maxim",
                    new News(UUID.randomUUID(), "07-08-2022 17:24", "Касаткина проиграла теннисный турнир",
                            "Теперь на ее счету четыре титулов на турнирах WTA.")),
            new Comment(UUID.randomUUID(), "06-08-2022 10:24", "Класс", "Anton",
                    new News(UUID.randomUUID(), "07-08-2022 17:24", "Касаткина была на теннисный турнир",
                            "Теперь на ее счету три титулов на турнирах WTA."))
    );

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Comment.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return validComment.get(new Random().nextInt(validComment.size()));
    }
}
