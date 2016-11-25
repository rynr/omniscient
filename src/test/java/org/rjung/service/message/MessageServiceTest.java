package org.rjung.service.message;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.rjung.service.helper.TestHelper.randomInt;
import static org.rjung.service.helper.TestHelper.randomPrincipal;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rjung.service.helper.TestHelper;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @Mock
    private MessageDao messageDao;
    @Mock
    private List<MessageInterceptor> messageInceptors;
    @InjectMocks
    private MessageService sut;

    @Captor
    private ArgumentCaptor<MessageDTO> captor;

    @Test
    public void verifyGetMessages() {
        List<MessageDTO> daoResult = Arrays.asList(TestHelper.randomMessageDTO());
        int page = randomInt(200);
        int limit = randomInt(200);
        Principal user = randomPrincipal();
        when(messageDao.getMessages(page, limit, user.getName())).thenReturn(daoResult);

        List<Message> result = sut.getMessages(page, limit, user);

        verify(messageDao).getMessages(page, limit, user.getName());
        verifySameResult(result, daoResult);
    }

    @Test
    public void verifyGetMessage() {
        MessageDTO expectedResult = TestHelper.randomMessageDTO();
        Principal principal = randomPrincipal();
        when(messageDao.getMessage(expectedResult.getId(), principal.getName())).thenReturn(expectedResult);

        Message result = sut.getMessage(expectedResult.getId(), principal);

        verify(messageDao).getMessage(expectedResult.getId(), principal.getName());
        assertThat(result, is(new Message(MessageType.valueOf(expectedResult.getType()), expectedResult.getContent(),
                LocalDateTime.ofEpochSecond(expectedResult.getCreatedAt(), 0, ZoneOffset.UTC))));
    }

    @Test
    public void verifySaveStoresEmptyStringAsLog() {
        Principal principal = randomPrincipal();

        sut.save("log", principal);

        verify(messageDao).save(captor.capture());
        MessageDTO message = captor.getValue();
        assertThat(message.getUser(), equalTo(principal.getName()));
        assertThat(message.getType(), equalTo(MessageType.LOG.name()));
        assertThat(message.getContent(), equalTo("log"));
    }

    @Test
    public void verifySaveTodoStoresTodo() {
        Principal principal = randomPrincipal();

        sut.save("[] todo", principal);

        verify(messageDao).save(captor.capture());
        MessageDTO message = captor.getValue();
        assertThat(message.getUser(), equalTo(principal.getName()));
        assertThat(message.getType(), equalTo(MessageType.TODO.name()));
        assertThat(message.getContent(), equalTo("todo"));
    }

    private void verifySameResult(List<Message> result, List<MessageDTO> daoResult) {
        assertThat(result.size(), equalTo(daoResult.size()));
        for (int i = 0; i < result.size(); i++) {
            Message message = result.get(i);
            MessageDTO messageDTO = daoResult.get(i);
            assertThat(message.getContent(), equalTo(messageDTO.getContent()));
            assertThat(message.getType().name(), equalTo(messageDTO.getType()));
            assertThat(message.getCreatedAt().toEpochSecond(ZoneOffset.UTC), equalTo(messageDTO.getCreatedAt()));
        }
    }
}
