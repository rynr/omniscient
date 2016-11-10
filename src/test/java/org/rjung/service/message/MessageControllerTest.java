package org.rjung.service.message;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.rjung.service.helper.TestHelper.randomMessage;
import static org.rjung.service.helper.TestHelper.randomString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MessageService messageService;

    @Test
    public void testPostMessage() throws Exception {
        String message = randomString(128);
        String uuid = randomString(16);
        when(this.messageService.save(message)).thenReturn(uuid);

        this.mvc.perform(post("/messages").contentType(MediaType.TEXT_PLAIN).content(message))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/messages/" + uuid));

        verify(messageService).save(message);
    }

    @Test
    public void testGetMessages() throws Exception {
        Message message = randomMessage();
        when(this.messageService.getMessages(2, 4)).thenReturn(Arrays.asList(message));

        this.mvc.perform(get("/messages?page=2&limit=4").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
                .andExpect(content().string(message.export()));

        verify(messageService).getMessages(2, 4);
    }

    @Test
    public void testHeadMessages() throws Exception {
        this.mvc.perform(head("/messages?page=2&limit=4").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk());

        verifyZeroInteractions(messageService);
    }
}
