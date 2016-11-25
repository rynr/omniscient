package org.rjung.service.message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

/**
 * A {@link Message} is the root object in omniscient. If not available a
 * creation-date will be derived of the current timestamp.
 */
public class Message {

    private final MessageType type;
    private final String content;
    private final LocalDateTime createdAt;

    /**
     * Build a new {@link Message}. The creation-time will be <tt>now</tt>.
     *
     * @param type
     *            {@link MessageType} of the {@link Message}.
     * @param content
     *            Content of the {@link Message}.
     */
    public Message(MessageType type, String content) {
        this(type, content, LocalDateTime.now());
    }

    /**
     * Build a new {@link Message}.
     *
     * @param type
     *            {@link MessageType} of the {@link Message}.
     * @param content
     *            Content of the {@link Message}.
     * @param createdAt
     *            Creation time of the {@link Message}.
     */
    public Message(MessageType type, String content, LocalDateTime createdAt) {
        this.type = type;
        this.content = content;
        this.createdAt = createdAt;
    }

    /**
     * Calculate a UUID of the {@link Message}. This value is persisted in the
     * database, to be able to retrieve it. The logic may never be changed or it
     * will not be possible to retrieve {@link Message}s
     *
     * @return
     */
    public UUID getId() {
        return UUID.nameUUIDFromBytes(
                (createdAt.toInstant(ZoneOffset.UTC).getEpochSecond() + type.name() + content).getBytes());
    }

    /**
     * Retrieve the {@link MessageType} of the {@link Message}.
     *
     * @return The {@link MessageType} of the {@link Message}.
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Retrieve the content of the {@link Message}.
     *
     * @return The content of the {@link Message}.
     */
    public String getContent() {
        return content;
    }

    /**
     * Retrieve the creation-time of the {@link Message}.
     *
     * @return The creation-time of the {@link Message}.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Retrieve the export format of the {@link Message}. This format will be
     * used to return the {@link Message} as plain test to the client.
     *
     * @return Export-format of the {@link Message}.
     */
    public String export() {
        return String.join("\t", getId().toString(),
                String.valueOf(createdAt.toInstant(ZoneOffset.UTC).getEpochSecond()), type.getIdentifier(),
                content.replaceAll("\n", "\\n"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, content, createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Message other = (Message) obj;
        return Objects.equals(this.type, other.type) && Objects.equals(this.content, other.content)
                && Objects.equals(this.createdAt, other.createdAt);
    }

    @Override
    public String toString() {
        return "Message[id=" + getId().toString() + ", created-at='" + createdAt.toString() + "', type=" + type.name()
                + ", message:'" + content.replaceAll("\n", "\\n") + "']";
    }
}
