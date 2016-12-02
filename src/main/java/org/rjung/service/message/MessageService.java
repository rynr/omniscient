package org.rjung.service.message;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service to store and retrieve {@link Message}s.
 */
@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messages;

    public MessageService(final MessageRepository messages) {
        this.messages = messages;
    }

    /**
     * Get a number of {@link Message}s.
     *
     * @param page
     *            The method will only return <tt>limit</tt> amount of
     *            {@link Message}s. To access more, the <tt>page</tt> parameter
     *            casn be increased to get another page of {@link Message}s. The
     *            value of the first page is <tt>1</tt>.
     * @param limit
     *            The retrieval should be limited to a number of
     *            {@link Message}s. The amount of {@link Message}s is set with
     *            the parameter <tt>limit</tt>. To access further
     *            {@link Message}s, use the parameter <tt>page</tt>.
     * @param principal
     *            The user of the {@link Message}s to be retrieved.
     * @return A list with maximum <tt>limit</tt> {@link Message}s. If none are
     *         available or the <tt>page</tt> is to high, there will be no
     *         results in the list.
     */
    public Page<Message> getMessages(Pageable page, final Principal principal) {
        LOGGER.debug("get message: principal " + principal);
        Page<MessageDTO> ms = messages.findByUserOrderByCreatedAtDesc(principal.getName(), page);
        return new PageImpl<>(ms.getContent().stream()
                .map(m -> new Message(MessageType.valueOf(m.getType()), m.getContent(),
                        LocalDateTime.ofEpochSecond(m.getCreatedAt(), 0, ZoneOffset.UTC)))
                .collect(Collectors.toList()), page, ms.getTotalPages());

    }

    /**
     * Get a {@link Message}s.
     *
     * @param principal
     *            The user of the {@link Message}s to be retrieved.
     * @return A list with maximum <tt>limit</tt> {@link Message}s. If none are
     *         available or the <tt>page</tt> is to high, there will be no
     *         results in the list.
     */
    public Message getMessage(String id, final Principal principal) {
        LOGGER.debug("get message: principal " + principal);
        MessageDTO message = messages.findOneByIdAndUser(id, principal.getName());
        return new Message(MessageType.valueOf(message.getType()), message.getContent(),
                LocalDateTime.ofEpochSecond(message.getCreatedAt(), 0, ZoneOffset.UTC));
    }

    /**
     * Persist a new {@link Message}. The {@link Message} is created from it's
     * plain text-source.
     *
     * @param source
     *            The text-source of the {@link Message}.
     * @return The <tt>id</tt> of the created {@link Message}. This can be used
     *         to retrieve the new {@link Message} again.
     */
    public String save(final String source, final Principal principal) {
        LOGGER.debug("save: " + source);
        Message message;
        MessageType type = MessageType.getMessageType(source);
        if (type == null) {
            type = MessageType.LOG;
            LOGGER.debug("  type is: " + type);
            message = new Message(type, source);
        } else {
            LOGGER.debug("  type is: " + type);
            message = new Message(type, type.clearMessage(source));
        }
        MessageDTO messageDTO = new MessageDTO(message, principal.getName());
        messages.index(messageDTO);
        return message.getId().toString();

    }

}
