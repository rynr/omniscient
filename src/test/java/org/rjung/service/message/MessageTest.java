package org.rjung.service.message;

import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.rjung.service.helper.RegexMatcher;
import org.rjung.service.helper.TestHelper;

public class MessageTest {

    @Test
    public void verifyExportFormatIsCorrect() {
        MessageType type = TestHelper.randomEnum(MessageType.values());
        String content = TestHelper.randomString(TestHelper.randomInt(255));
        assertThat(new Message(type, content).export(),
                RegexMatcher.matchesRegex(
                        String.join("\\t", "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", "\\d+",
                                "("
                                        + String.join("|", Stream.of(MessageType.values())
                                                .map(MessageType::getIdentifier).collect(Collectors.toList()))
                                        + ")",
                                Pattern.quote(content))));
    }
}
