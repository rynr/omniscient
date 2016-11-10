package org.rjung.service.tag;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rjung.service.message.MessageDTO;

@RunWith(MockitoJUnitRunner.class)
public class MessageInterceptorTagExtractionTest {

    @Mock
    TagDao tagdao;
    @InjectMocks
    private MessageInterceptorTagExtraction sut;

    @Test
    public void expectToStoreFoundTagToDao() {
        MessageDTO message = new MessageDTO("1", 1l, "Log", "some content with some #tag");
        sut.saveMessage(message);
        
        verify(tagdao).insertTag("#tag", "1");
    }
}
