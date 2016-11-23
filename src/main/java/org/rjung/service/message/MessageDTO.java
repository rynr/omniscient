package org.rjung.service.message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * The {@link MessageDTO} contains the data of a {@link Message} to be stored in
 * the database.
 */
public class MessageDTO {

    private final String id;
    private final String user;
    private final long createdAt;
    private final String type;
    private final String content;

    /**
     * Create a new {@link MessageDTO} instance.
     *
     * @param id
     *            The id of the {@link MessageDTO} ({@link Message} does
     *            calculate the id from the internal data, to be able to query
     *            it, it needs to be stored).
     * @param createdAt
     *            The timestamp of the creation in seconds unixepoch
     *            ({@link Message} stores a {@link LocalDateTime}).
     * @param type
     *            The {@link MessageType} of the {@link MessageDTO} (here it's
     *            stored as a {@link String}, while {@link Message} stored the
     *            real enum).
     * @param content
     *            The textual content of the message.
     */
    public MessageDTO(String id, String user, long createdAt, String type, String content) {
        this.id = id;
        this.user = user;
        this.createdAt = createdAt;
        this.type = type;
        this.content = content;
    }

    /**
     * Create a new {@link MessageDTO} from a given {@link Message}.
     *
     * @param message
     *            The Message to be built as {@link MessageDTO}.
     */
    public MessageDTO(Message message, String user) {
        this.id = message.getId().toString();
        this.user = user;
        this.createdAt = message.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
        this.type = message.getType().name();
        this.content = message.getContent();
    }

    /**
     * Retrieve the id of the {@link MessageDTO}. The id is calculated by the
     * {@link Message} depending on it's values.
     *
     * @return The id of the {@link MessageDTO}.
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieve the <tt>username</tt> of the {@link MessageDTO}.
     *
     * @return The <tt>username</tt> of the {@link MessageDTO}.
     */
    public String getUser() {
        return user;
    }

    /**
     * Retrieve the creation time of the {@link MessageDTO} in seconds since
     * unixepoch. {@link Message} stores a {@link LocalDateTime} WHILE
     * {@link MessageDTO} stores the seconds since unixepoch.
     *
     * @return The creation time of the {@link MessageDTO} in seconds since
     *         unixepoch.
     */
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * Retrieve the {@link MessageType} of the {@link MessageDTO}.
     * {@link Message} stores a {@link MessageType} value, {@link MessageDTO}
     * only stores a {@link String} with the name.
     *
     * @return The {@link MessageType} of the {@link MessageDTO}.
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieve the content of the {@link MessageDTO}.
     *
     * @return The content of the {@link MessageDTO}.
     */
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.join("\t", id, String.valueOf(createdAt), type, content);
    }
}
