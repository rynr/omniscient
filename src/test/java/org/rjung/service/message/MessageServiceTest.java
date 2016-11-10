package org.rjung.service.message;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(messageDao.getMessages(1, 10)).thenReturn(daoResult);

        List<Message> result = sut.getMessages(1, 10);

        verify(messageDao).getMessages(1, 10);
        verifySameResult(result, daoResult);
    }

    @Test
    public void verifySaveStoresEmptyStringAsLog() {
        sut.save("log");

        verify(messageDao).save(captor.capture());
        MessageDTO message = captor.getValue();
        assertThat(message.getType(), equalTo(MessageType.LOG.name()));
        assertThat(message.getContent(), equalTo("log"));
    }

    @Test
    public void verifySaveTodoStoresTodo() {
        sut.save("[] todo");

        verify(messageDao).save(captor.capture());
        MessageDTO message = captor.getValue();
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
