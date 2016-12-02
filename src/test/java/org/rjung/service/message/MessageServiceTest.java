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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private MessageService sut;

    @Captor
    private ArgumentCaptor<MessageDTO> captor;

    @Test
    public void verifyGetMessages() {
        int page = randomInt(200);
        int limit = randomInt(200);
        PageRequest paging = new PageRequest(page, limit);
        Page<MessageDTO> daoResult = new PageImpl<>(Arrays.asList(TestHelper.randomMessageDTO()), paging, 1);
        Principal user = randomPrincipal();
        when(messageRepository.findByUserOrderByCreatedAtDesc(user.getName(), paging)).thenReturn(daoResult);

        Page<Message> result = sut.getMessages(paging, user);

        verify(messageRepository).findByUserOrderByCreatedAtDesc(user.getName(), paging);
        verifySameResult(result, daoResult);
    }

    @Test
    public void verifyGetMessage() {
        MessageDTO expectedResult = TestHelper.randomMessageDTO();
        Principal principal = randomPrincipal();
        when(messageRepository.findOneByIdAndUser(expectedResult.getId(), principal.getName()))
                .thenReturn(expectedResult);

        Message result = sut.getMessage(expectedResult.getId(), principal);

        verify(messageRepository).findOneByIdAndUser(expectedResult.getId(), principal.getName());
        assertThat(result, is(new Message(MessageType.valueOf(expectedResult.getType()), expectedResult.getContent(),
                LocalDateTime.ofEpochSecond(expectedResult.getCreatedAt(), 0, ZoneOffset.UTC))));
    }

    @Test
    public void verifySaveStoresEmptyStringAsLog() {
        Principal principal = randomPrincipal();

        sut.save("log", principal);

        verify(messageRepository).index(captor.capture());
        MessageDTO message = captor.getValue();
        assertThat(message.getUser(), equalTo(principal.getName()));
        assertThat(message.getType(), equalTo(MessageType.LOG.name()));
        assertThat(message.getContent(), equalTo("log"));
    }

    @Test
    public void verifySaveTodoStoresTodo() {
        Principal principal = randomPrincipal();

        sut.save("[] todo", principal);

        verify(messageRepository).index(captor.capture());
        MessageDTO message = captor.getValue();
        assertThat(message.getUser(), equalTo(principal.getName()));
        assertThat(message.getType(), equalTo(MessageType.TODO.name()));
        assertThat(message.getContent(), equalTo("todo"));
    }

    private void verifySameResult(Page<Message> result, Page<MessageDTO> daoResult) {
        assertThat(result.getSize(), equalTo(daoResult.getSize()));
        assertThat(result.getNumber(), equalTo(daoResult.getNumber()));
        assertThat(result.getNumberOfElements(), equalTo(daoResult.getNumberOfElements()));
        assertThat(result.getTotalElements(), equalTo(daoResult.getTotalElements()));
        assertThat(result.getTotalPages(), equalTo(daoResult.getTotalPages()));
        List<Message> content = result.getContent();
        List<MessageDTO> daoContent = daoResult.getContent();
        for (int i = 0; i < content.size(); i++) {
            Message message = content.get(i);
            MessageDTO messageDTO = daoContent.get(i);
            assertThat(message.getContent(), equalTo(messageDTO.getContent()));
            assertThat(message.getType().name(), equalTo(messageDTO.getType()));
            assertThat(message.getCreatedAt().toEpochSecond(ZoneOffset.UTC), equalTo(messageDTO.getCreatedAt()));
        }
    }
}
