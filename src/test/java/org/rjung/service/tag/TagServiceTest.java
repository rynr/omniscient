package org.rjung.service.tag;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TagServiceTest {

    TagService sut;

    @Before
    public void setup() {
        this.sut = new TagService();
    }

    @Test
    public void verifyGetTagsFromMessageWorks() {
        assertThat(sut.getTagsFromMessage("").size(), equalTo(0));
        assertThat(sut.getTagsFromMessage("lorem ipsum dolor sit amet...").size(), equalTo(0));
        assertElements(sut.getTagsFromMessage("hello #tag"), "tag");
        assertElements(sut.getTagsFromMessage("#hello #tag #and #mode"), "hello", "tag", "and", "mode");
        assertElements(sut.getTagsFromMessage("bla #089 sdasd"), "089");
    }

    private void assertElements(Set<String> tagsFromMessage, String... string) {
        assertThat(tagsFromMessage.size(), equalTo(string.length));
        for (String tag : string) {
            assertThat(tagsFromMessage.contains(tag), is(true));
        }
    }
}
