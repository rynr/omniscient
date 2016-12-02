package org.rjung.service.message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@link MessageDTO} contains the data of a {@link Message} to be stored in
 * the database.
 */
@Document(indexName = "messages", type = "message")
public class MessageDTO {

    @Id
    private final String id;
    @Field(index = FieldIndex.not_analyzed, store = false, type = FieldType.String)
    private final String user;
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.String)
    private final String type;
    @Field(index = FieldIndex.analyzed, store = true, type = FieldType.String)
    private final String content;
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.Long)
    private final long createdAt;

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
    @JsonCreator
    public MessageDTO(@JsonProperty("id") final String id, @JsonProperty("user") final String user,
            @JsonProperty("type") final String type, @JsonProperty("content") final String content,
            @JsonProperty("createdAt") final long createdAt) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.content = content;
        this.createdAt = createdAt;
    }

    /**
     * Create a new {@link MessageDTO} from a given {@link Message}.
     *
     * @param message
     *            The Message to be built as {@link MessageDTO}.
     */
    public MessageDTO(final Message message, final String user) {
        this.id = message.getId().toString();
        this.user = user;
        this.type = message.getType().name();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
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

    @Override
    public int hashCode() {
        return Objects.hash(id, user, type, content, createdAt);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageDTO other = (MessageDTO) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.user, other.user)
                && Objects.equals(this.type, other.type) && Objects.equals(this.content, other.content)
                && Objects.equals(this.createdAt, other.createdAt);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("id", id).append("user", user).append("type", type)
                .append("content", content).append("createdAt", createdAt).toString();
    }
}
