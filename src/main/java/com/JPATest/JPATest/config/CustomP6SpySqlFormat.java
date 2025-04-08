package com.JPATest.JPATest.config;

import com.JPATest.JPATest.util.QueryCountHolder;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class CustomP6SpySqlFormat implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed,
                                String category, String prepared, String sql, String url) {
        if (sql == null || sql.trim().isEmpty()) return "";

        QueryCountHolder.increment(); // 여기서 count 증가
        return elapsed + "ms | " + sql.replaceAll("\\s+", " ");
    }
}