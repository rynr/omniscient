package org.rjung.service.tag;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao {

    final JdbcTemplate jdbcTemplate;

    public TagDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getTags() {
        return jdbcTemplate.queryForList("SELECT name FROM tags ORDER BY name", String.class);
    }

    public List<String> searchTags(final String start) {
        return jdbcTemplate.queryForList("SELECT name FROM tags WHERE name LIKE ? ORDER BY name",
                new Object[] { start + "%" }, String.class);
    }

    public List<TagUsage> getTagUsage() {
        return jdbcTemplate.query(
                "SELECT tags.name, count(*) FROM tags JOIN messages_tags ON messages_tags.tag_id = tags.id GROUP BY tags.name",
                (rs, rowNum) -> new TagUsage(rs.getString(1), rs.getInt(2)));
    }

    public void insertTag(final String tag, final String messageId) {
        long tagId = getIdOfTag(tag);
        jdbcTemplate.update("INSERT INTO messages_tags (message_id, tag_id) VALUES (?, ?)", messageId, tagId);
    }

    private long getIdOfTag(final String tag) {
        Integer numberOfTags = jdbcTemplate.queryForObject("SELECT COUNT(name) FROM tags WHERE name = ?",
                new String[] { tag }, Integer.class);
        if (numberOfTags > 0) {
            return jdbcTemplate.queryForObject("SELECT id FROM tags WHERE name = ?", new String[] { tag }, Long.class);
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO tags (name) VALUES (?)",
                        new String[] { "id" });
                ps.setString(1, tag);
                return ps;
            }, keyHolder);
            return keyHolder.getKey().longValue();
        }
    }
}
