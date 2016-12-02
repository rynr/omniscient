package org.rjung.service.message;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.rjung.service.helper.TestHelper.randomEnum;
import static org.rjung.service.helper.TestHelper.randomString;
import static org.rjung.service.helper.TestHelper.randomTime;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.rjung.service.helper.RegexMatcher;
import org.rjung.service.helper.TestHelper;

public class MessageTest {

    @Test
    public void verifyExportFormatIsCorrect() {
        MessageType type = randomEnum(MessageType.values());
        String content = randomString(TestHelper.randomInt(255));
        assertThat(new Message(type, content).export(),
                RegexMatcher
                        .matchesRegex(String.join("\\t", "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
                                "\\d+", "(" + String.join("|", Stream.of(MessageType.values())
                                        .map(MessageType::getIdentifier).collect(Collectors.toList())) + ")",
                                Pattern.quote(content))));
    }

    @Test
    public void verifyUuidIsSameFor() {
        MessageType type = randomEnum(MessageType.values());
        String content = randomString(TestHelper.randomInt(255));
        LocalDateTime createdAt = randomTime();
        assertThat(new Message(type, content, createdAt).getId(),
                equalTo(new Message(type, content, createdAt).getId()));
    }
}
