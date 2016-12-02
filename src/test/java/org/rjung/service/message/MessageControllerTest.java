package org.rjung.service.message;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.rjung.service.helper.TestHelper.randomEnum;
import static org.rjung.service.helper.TestHelper.randomInt;
import static org.rjung.service.helper.TestHelper.randomMessage;
import static org.rjung.service.helper.TestHelper.randomPrincipal;
import static org.rjung.service.helper.TestHelper.randomString;

import java.security.Principal;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    MessageController sut; // System under test

    @Test
    public void verifyHeadMessagesReturnsStatusOk() {
        ResponseEntity<Void> result = sut.headMessages();

        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void verifyGetMessagesTextDelegatesToService() {
        PageRequest page = randomPage();
        Principal principal = randomPrincipal();
        int expectedResults = randomInt(page.getPageSize());
        Page<Message> expectedResult = new PageImpl<>(
                IntStream.range(0, expectedResults).mapToObj(i -> randomMessage()).collect(Collectors.toList()), page,
                page.getPageSize() + randomInt(400));
        when(messageService.getMessages(page, principal)).thenReturn(expectedResult);

        ResponseEntity<String> result = sut.getMessagesText(page, principal);

        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().split("\n").length, is(expectedResults));
        // Perhaps there's a better way than doing the same as in the sut.
        assertThat(result.getBody(),
                is(expectedResult.getContent().stream().map(m -> m.export()).collect(Collectors.joining("\n"))));
        verify(messageService).getMessages(page, principal);
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void verifyGetMessageTextDelegatesToService() {
        Principal principal = randomPrincipal();
        Message message = randomMessage();
        when(messageService.getMessage(message.getId().toString(), principal)).thenReturn(message);

        ResponseEntity<String> result = sut.getMessageText(message.getId().toString(), principal);

        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        // Perhaps there's a better way than doing the same as in the sut.
        assertThat(result.getBody(), is(message.export()));
        verify(messageService).getMessage(message.getId().toString(), principal);
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void verifyPostMessageTextDelegatesToService() {
        MessageType messageType = randomEnum(MessageType.values());
        String messageBody = randomString(12);
        String source = messageType.getIdentifier() + " " + messageBody;
        Message message = new Message(messageType, messageBody);
        Principal principal = randomPrincipal();
        when(messageService.save(source, principal)).thenReturn(message.getId().toString());

        ResponseEntity<Void> response = sut.postMessageText(source,
                UriComponentsBuilder.fromHttpUrl("https://www.example.com/"), principal);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getHeaders().get("Location").get(0),
                is("https://www.example.com/messages/" + message.getId() + ".txt"));
        verify(messageService).save(source, principal);
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void verifyGetMessagesHtmlDelegatesToService() {
        PageRequest page = randomPage();
        Principal principal = randomPrincipal();
        int expectedResults = randomInt(page.getPageSize());
        Page<Message> expectedResult = new PageImpl<>(
                IntStream.range(0, expectedResults).mapToObj(i -> randomMessage()).collect(Collectors.toList()), page,
                page.getPageSize() + randomInt(400));
        when(messageService.getMessages(page, principal)).thenReturn(expectedResult);

        ModelAndView result = sut.getMessagesHtml(page, principal);

        assertThat(result.getViewName(), is("messages"));
        assertThat(result.getModel().get("messages"), is(expectedResult));
        verify(messageService).getMessages(page, principal);
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void verifyPostMessageHtmlDelegatesToService() {
        MessageType messageType = randomEnum(MessageType.values());
        String messageBody = randomString(12);
        String source = messageType.getIdentifier() + " " + messageBody;
        Message message = new Message(messageType, messageBody);
        Principal principal = randomPrincipal();
        when(messageService.save(source, principal)).thenReturn(message.getId().toString());

        RedirectView response = sut.postMessageHtml(source,
                UriComponentsBuilder.fromHttpUrl("https://www.example.com/"), principal);

        assertThat(response.getUrl(), is("https://www.example.com/messages.html"));
        verify(messageService).save(source, principal);
        verifyNoMoreInteractions(messageService);

    }

    private PageRequest randomPage() {
        int page = randomInt(200);
        int size = randomInt(200);
        return new PageRequest(page, size);
    }
}
