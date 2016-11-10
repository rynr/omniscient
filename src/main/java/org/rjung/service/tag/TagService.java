package org.rjung.service.tag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private TagDao tagDao;

    public List<String> getTags() {
        return tagDao.getTags();
    }

    public List<String> searchTags(String start) {
        return tagDao.searchTags(start);
    }

    public List<TagUsage> getTagUsage() {
        return tagDao.getTagUsage();
    }

    public void insertTag(String tag, String messageId) {
        tagDao.insertTag(tag, messageId);
    }

    public Set<String> getTagsFromMessage(String message) {
        Set<String> result = new HashSet<>();
        Pattern pattern = Pattern.compile("#([\\w]+)");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }
}
