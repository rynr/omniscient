package org.rjung.service.tag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rjung.service.message.MessageDTO;
import org.rjung.service.message.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageInterceptorTagExtraction implements MessageInterceptor {

    private static final Pattern TAG_PATTERN = Pattern.compile("(?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+)");

    @Autowired
    private TagDao tagDao;

    @Override
    public void saveMessage(MessageDTO message) {
        Matcher matcher = TAG_PATTERN.matcher(message.getContent());
        while (matcher.find()) {
            tagDao.insertTag(matcher.group(1), message.getId());
        }
    }

}
