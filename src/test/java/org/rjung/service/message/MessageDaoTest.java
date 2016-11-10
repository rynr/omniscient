package org.rjung.service.message;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.rjung.service.helper.TestHelper.randomInt;
import static org.rjung.service.helper.TestHelper.randomMessageDTO;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MessageDao.class)
public class MessageDaoTest {

    @Autowired
    private MessageDao sut;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    public void verifySaveDelegatesToJdbcTemplate() {
        MessageDTO messageDTO = randomMessageDTO();

        sut.save(messageDTO);

        verify(jdbcTemplate).update("INSERT INTO messages (id, created_at, type, body) VALUES (?, ?, ?, ?)",
                messageDTO.getId(), messageDTO.getCreatedAt(), messageDTO.getType(), messageDTO.getContent());
    }

    @Test
    public void verifyGetMessagesDelegatesToJdbcTemplate() {
        int page = randomInt(10);
        int limit = randomInt(10);
        List<MessageDTO> expectedMessages = Arrays.asList(randomMessageDTO());
        when(jdbcTemplate.query(any(String.class), any(Object[].class), Mockito.<RowMapper<MessageDTO>>any()))
                .thenReturn(expectedMessages);

        List<MessageDTO> messages = sut.getMessages(page, limit);

        assertThat(messages, is(expectedMessages));
    }

}
