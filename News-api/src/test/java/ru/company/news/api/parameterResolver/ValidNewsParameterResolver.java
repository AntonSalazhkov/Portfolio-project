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

public class ValidNewsParameterResolver implements ParameterResolver {

    public static List<News> validNews = Arrays.asList(
            new News(UUID.randomUUID(), "07-09-2022 17:24", "Касаткина выиграла теннисный турнир",
                    "Теперь на ее счету пять титулов на турнирах WTA."),
            new News(UUID.randomUUID(), "07-10-2022 17:24", "Касаткина выиграла",
                    "Теперь на ее счету пять титулов на турнирах WTA."),
            new News(UUID.randomUUID(), "07-11-2022 17:24", "Касаткина выиграла теннисный турнир",
                    "Теперь на ее счету три титула на турнирах WTA.")

    );

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == News.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        News news = validNews.get(new Random().nextInt(validNews.size()));
        news.getComments().add(new Comment(UUID.randomUUID(), "09-08-2022 17:34", "Хорошая новинка", "Anton", news));
        return news;
    }
}
