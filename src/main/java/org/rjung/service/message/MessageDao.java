package org.rjung.service.message;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Persist and retrieve {@link MessageDTO}s to and from the database.
 */
@Repository
public class MessageDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDao.class);

    JdbcTemplate jdbcTemplate;

    @Autowired
    MessageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Store a {@link MessageDTO} in the database.
     *
     * @param message
     *            The {@link MessageDTO} to be stored
     * @return The number of updates, so usually this will be <tt>1</tt>.
     */
    public int save(MessageDTO message) {
        LOGGER.debug("save: " + message);
        return jdbcTemplate.update("INSERT INTO messages (id, user, created_at, type, body) VALUES (?, ?, ?, ?, ?)",
                message.getId(), message.getUser(), message.getCreatedAt(), message.getType(), message.getContent());
    }

    /**
     * Retrieve a paginated list of {@link MessageDTO} from the database. The
     * order is always by time descending.
     *
     * @param page
     *            The page that should be requested.
     * @param limit
     *            The maximum number of {@link MessageDTO}.
     * @return The ordered list of {@link MessageDTO}. It can be empty, if the
     *         <tt>page</tt> is to high or there are just no {@link MessageDTO}s
     *         in the database.
     */
    public List<MessageDTO> getMessages(int page, int limit) {
        LOGGER.debug("get messages: page " + page + ", limit " + limit);
        return jdbcTemplate.query(
                "SELECT id, user, created_at, type, body FROM messages ORDER BY created_at DESC LIMIT ?, ?",
                new Object[] { (page - 1) * limit, limit }, (rs, rowNum) -> new MessageDTO(rs.getString(1),
                        rs.getString(2), rs.getLong(3), rs.getString(4), rs.getString(5)));
    }
}
