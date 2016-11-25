package org.rjung.service.message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Persist and retrieve {@link MessageDTO}s to and from the database.
 */
@Repository
public class MessageDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDao.class);

    private final JdbcTemplate jdbcTemplate;
    private MessageDTOMapper messageMapper;

    @Autowired
    MessageDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        messageMapper = new MessageDTOMapper();
    }

    /**
     * Store a {@link MessageDTO} in the database.
     *
     * @param message
     *            The {@link MessageDTO} to be stored
     * @return The number of updates, so usually this will be <tt>1</tt>.
     */
    public int save(final MessageDTO message) {
        LOGGER.debug("save: " + message);
        return jdbcTemplate.update("INSERT INTO messages (id, user, type, body, created_at) VALUES (?, ?, ?, ?, ?)",
                message.getId(), message.getUser(), message.getType(), message.getContent(), message.getCreatedAt());
    }

    /**
     * Retrieve a {@link MessageDTO} from the database.
     *
     * @param id
     *            The <tt>id</tt> of the message that should be requested.
     * @param user
     *            The username of who the {@link MessageDTO}s will be retrieved.
     * @return The {@link MessageDTO}.
     */
    public MessageDTO getMessage(final String id, final String user) {
        LOGGER.debug("get message: " + id);
        return jdbcTemplate.queryForObject(
                "SELECT id, user, type, body, created_at FROM messages WHERE id = ? AND user = ?",
                new Object[] { id, user }, messageMapper);
    }

    /**
     * Retrieve a paginated list of {@link MessageDTO} from the database. The
     * order is always by time descending.
     *
     * @param page
     *            The page that should be requested.
     * @param limit
     *            The maximum number of {@link MessageDTO}.
     * @param user
     *            The username of who the {@link MessageDTO}s will be retrieved.
     * @return The ordered list of {@link MessageDTO}. It can be empty, if the
     *         <tt>page</tt> is to high or there are just no {@link MessageDTO}s
     *         in the database.
     */
    public List<MessageDTO> getMessages(final int page, final int limit, final String user) {
        LOGGER.debug("get messages: page " + page + ", limit " + limit);
        return jdbcTemplate.query(
                "SELECT id, user, type, body, created_at FROM messages WHERE user = ? ORDER BY created_at DESC LIMIT ?, ?",
                new Object[] { user, (page - 1) * limit, limit }, messageMapper);
    }

    private class MessageDTOMapper implements RowMapper<MessageDTO> {

        @Override
        public MessageDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MessageDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getLong(5));
        }

    }
}
