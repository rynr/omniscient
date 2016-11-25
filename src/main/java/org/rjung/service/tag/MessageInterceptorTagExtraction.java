package org.rjung.service.tag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rjung.service.message.MessageDTO;
import org.rjung.service.message.MessageInterceptor;
import org.springframework.stereotype.Component;

@Component
public class MessageInterceptorTagExtraction implements MessageInterceptor {

    private static final Pattern TAG_PATTERN = Pattern.compile("(?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+)");

    private final TagDao tagDao;

    public MessageInterceptorTagExtraction(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public void saveMessage(final MessageDTO message) {
        Matcher matcher = TAG_PATTERN.matcher(message.getContent());
        while (matcher.find()) {
            tagDao.insertTag(matcher.group(1), message.getId());
        }
    }

}
