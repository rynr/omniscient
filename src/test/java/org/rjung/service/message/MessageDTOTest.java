package org.rjung.service.message;

import static org.junit.Assert.assertEquals;
import static org.rjung.service.helper.TestHelper.randomMessage;
import static org.rjung.service.helper.TestHelper.randomString;

import java.time.ZoneOffset;

import org.junit.Test;

public class MessageDTOTest {

    @Test
    public void verifyConstructorTakesValuesFromMessage() {
        Message message = randomMessage();
        String user = randomString(36);

        MessageDTO messageDTO = new MessageDTO(message, user);

        assertEquals(messageDTO.getId(), message.getId().toString());
        assertEquals(messageDTO.getUser(), user);
        assertEquals(messageDTO.getContent(), message.getContent());
        assertEquals(messageDTO.getType(), message.getType().name());
        assertEquals(messageDTO.getCreatedAt(), message.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
    }
}
