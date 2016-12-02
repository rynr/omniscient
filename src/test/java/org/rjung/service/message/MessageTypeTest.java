package org.rjung.service.message;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Test;

public class MessageTypeTest {

    @Test
    public void verifyLogMatches() {
        MessageType type = MessageType.LOG;
        assertThat(type.match("-message"), is(true));
        assertThat(type.match("- message"), is(true));
        assertThat(type.match(" -message"), is(true));
        assertThat(type.match(" - message"), is(true));
        assertThat(type.match("      -        message"), is(true));
        assertThat(type.match("≣message"), is(true));
        assertThat(type.match("≣ message"), is(true));
        assertThat(type.match(" ≣message"), is(true));
        assertThat(type.match(" ≣ message"), is(true));
        assertThat(type.match("      ≣        message"), is(true));

        assertThat(type.match(" i  todo"), is(false));
        assertThat(type.match(" I  done"), is(false));
        assertThat(type.match(" !  descision"), is(false));
        assertThat(type.match("[ ] todo"), is(false));
        assertThat(type.match("[x] done"), is(false));
        assertThat(type.match("[X] done"), is(false));
        assertThat(type.match(" >  bookmark"), is(false));
        assertThat(type.match(" $  snippet"), is(false));
        assertThat(type.match("ℹ done"), is(false));
        assertThat(type.match("‼ descision"), is(false));
        assertThat(type.match("☐ todo"), is(false));
        assertThat(type.match("☑ done"), is(false));
        assertThat(type.match("❯  bookmark"), is(false));
        assertThat(type.match("📖 snippet"), is(false));
    }

    @Test
    public void verifyInfoMatches() {
        MessageType type = MessageType.INFO;
        assertThat(type.match("imessage"), is(true));
        assertThat(type.match("i message"), is(true));
        assertThat(type.match(" imessage"), is(true));
        assertThat(type.match(" i message"), is(true));
        assertThat(type.match("      i        message"), is(true));
        assertThat(type.match("Imessage"), is(true));
        assertThat(type.match("I message"), is(true));
        assertThat(type.match(" Imessage"), is(true));
        assertThat(type.match(" I message"), is(true));
        assertThat(type.match("      I        message"), is(true));
        assertThat(type.match("ℹmessage"), is(true));
        assertThat(type.match("ℹ message"), is(true));
        assertThat(type.match(" ℹmessage"), is(true));
        assertThat(type.match(" ℹ message"), is(true));
        assertThat(type.match("      ℹ        message"), is(true));

        assertThat(type.match("[ ] todo"), is(false));
        assertThat(type.match("[x] done"), is(false));
        assertThat(type.match("[X] done"), is(false));
        assertThat(type.match(" !  descision"), is(false));
        assertThat(type.match(" >  bookmark"), is(false));
        assertThat(type.match(" $  snippet"), is(false));
        assertThat(type.match("‼ descision"), is(false));
        assertThat(type.match("☐ todo"), is(false));
        assertThat(type.match("☑ done"), is(false));
        assertThat(type.match("❯ bookmark"), is(false));
        assertThat(type.match("📖 snippet"), is(false));
    }

    @Test
    public void verifyDescisionMatches() {
        MessageType type = MessageType.DECISION;
        assertThat(type.match("!descision"), is(true));
        assertThat(type.match("! descision"), is(true));
        assertThat(type.match(" !descision"), is(true));
        assertThat(type.match(" ! descision"), is(true));
        assertThat(type.match("      !        descision"), is(true));
        assertThat(type.match("‼descision"), is(true));
        assertThat(type.match("‼ descision"), is(true));
        assertThat(type.match(" ‼descision"), is(true));
        assertThat(type.match(" ‼ descision"), is(true));
        assertThat(type.match("      ‼        descision"), is(true));

        assertThat(type.match("[ ] todo"), is(false));
        assertThat(type.match("[x] done"), is(false));
        assertThat(type.match("[X] done"), is(false));
        assertThat(type.match(" i  info"), is(false));
        assertThat(type.match(" I  info"), is(false));
        assertThat(type.match(" >  bookmark"), is(false));
        assertThat(type.match(" $  snippet"), is(false));
        assertThat(type.match("ℹ done"), is(false));
        assertThat(type.match("☐ todo"), is(false));
        assertThat(type.match("☑ done"), is(false));
        assertThat(type.match("❯ bookmark"), is(false));
        assertThat(type.match("📖 snippet"), is(false));
    }

    @Test
    public void verifyBookmarkMatches() {
        MessageType type = MessageType.BOOKMARK;
        assertThat(type.match("❯message"), is(true));
        assertThat(type.match("❯ message"), is(true));
        assertThat(type.match(" ❯message"), is(true));
        assertThat(type.match(" ❯ message"), is(true));
        assertThat(type.match("      ❯        message"), is(true));
        assertThat(type.match("> message"), is(true));
        assertThat(type.match(" > message"), is(true));
        assertThat(type.match(" >message"), is(true));
        assertThat(type.match("      >        message"), is(true));

        assertThat(type.match(" -  log"), is(false));
        assertThat(type.match(" i  todo"), is(false));
        assertThat(type.match(" I  done"), is(false));
        assertThat(type.match(" !  descision"), is(false));
        assertThat(type.match(" ‼  descision"), is(false));
        assertThat(type.match("[ ] todo"), is(false));
        assertThat(type.match("[x] done"), is(false));
        assertThat(type.match("[X] done"), is(false));
        assertThat(type.match(" $  snippet"), is(false));
        assertThat(type.match("≣ log"), is(false));
        assertThat(type.match("ℹ  done"), is(false));
        assertThat(type.match("☐ todo"), is(false));
        assertThat(type.match("☑ done"), is(false));
        assertThat(type.match("¶ snippet"), is(false));
    }

    @Test
    public void verifyTodoMatches() {
        MessageType type = MessageType.TODO;
        assertThat(type.match("☐message"), is(true));
        assertThat(type.match("☐ message"), is(true));
        assertThat(type.match(" ☐message"), is(true));
        assertThat(type.match(" ☐ message"), is(true));
        assertThat(type.match("      ☐        message"), is(true));
        assertThat(type.match("[ ] message"), is(true));
        assertThat(type.match("[ ] message"), is(true));
        assertThat(type.match(" [ ]message"), is(true));
        assertThat(type.match(" [ ] message"), is(true));
        assertThat(type.match("      [ ]        message"), is(true));
        assertThat(type.match("[] message"), is(true));
        assertThat(type.match("[] message"), is(true));
        assertThat(type.match(" []message"), is(true));
        assertThat(type.match(" [] message"), is(true));
        assertThat(type.match("      []        message"), is(true));

        assertThat(type.match(" -  log"), is(false));
        assertThat(type.match(" i  todo"), is(false));
        assertThat(type.match(" I  done"), is(false));
        assertThat(type.match(" !  descision"), is(false));
        assertThat(type.match(" ‼  descision"), is(false));
        assertThat(type.match("[x] done"), is(false));
        assertThat(type.match("[X] done"), is(false));
        assertThat(type.match(" >  bookmark"), is(false));
        assertThat(type.match(" $  snippet"), is(false));
        assertThat(type.match("≣ log"), is(false));
        assertThat(type.match("ℹ  done"), is(false));
        assertThat(type.match("☑ done"), is(false));
        assertThat(type.match("❯ bookmark"), is(false));
        assertThat(type.match("¶ snippet"), is(false));
    }

    @Test
    public void verifyDoneMatches() {
        MessageType type = MessageType.DONE;
        assertThat(type.match("☑message"), is(true));
        assertThat(type.match("☑ message"), is(true));
        assertThat(type.match(" ☑message"), is(true));
        assertThat(type.match(" ☑ message"), is(true));
        assertThat(type.match("      ☑        message"), is(true));
        assertThat(type.match("[x] message"), is(true));
        assertThat(type.match("[x] message"), is(true));
        assertThat(type.match(" [x]message"), is(true));
        assertThat(type.match(" [x] message"), is(true));
        assertThat(type.match("      [x]        message"), is(true));
        assertThat(type.match("[X] message"), is(true));
        assertThat(type.match("[X] message"), is(true));
        assertThat(type.match(" [X]message"), is(true));
        assertThat(type.match(" [X] message"), is(true));
        assertThat(type.match("      [X]        message"), is(true));

        assertThat(type.match(" -  log"), is(false));
        assertThat(type.match(" i  todo"), is(false));
        assertThat(type.match(" I  done"), is(false));
        assertThat(type.match(" !  descision"), is(false));
        assertThat(type.match(" ‼  descision"), is(false));
        assertThat(type.match("[ ] todo"), is(false));
        assertThat(type.match(" >  bookmark"), is(false));
        assertThat(type.match(" $  snippet"), is(false));
        assertThat(type.match("≣ log"), is(false));
        assertThat(type.match("ℹ  done"), is(false));
        assertThat(type.match("☐ todo"), is(false));
        assertThat(type.match("❯ bookmark"), is(false));
        assertThat(type.match("¶ snippet"), is(false));
    }

    @Test
    public void verifySnippetMatches() {
        MessageType type = MessageType.SNIPPET;
        assertThat(type.match("¶message"), is(true));
        assertThat(type.match("¶ message"), is(true));
        assertThat(type.match(" ¶message"), is(true));
        assertThat(type.match(" ¶ message"), is(true));
        assertThat(type.match("      ¶        message"), is(true));
        assertThat(type.match("$message"), is(true));
        assertThat(type.match("$ message"), is(true));
        assertThat(type.match(" $message"), is(true));
        assertThat(type.match(" $ message"), is(true));
        assertThat(type.match("      $        message"), is(true));

        assertThat(type.match(" -  log"), is(false));
        assertThat(type.match(" !  descision"), is(false));
        assertThat(type.match("[ ] todo"), is(false));
        assertThat(type.match("[x] done"), is(false));
        assertThat(type.match("[X] done"), is(false));
        assertThat(type.match(" >  bookmark"), is(false));
        assertThat(type.match("≣ log"), is(false));
        assertThat(type.match("ℹ  done"), is(false));
        assertThat(type.match("‼ descision"), is(false));
        assertThat(type.match("☐ todo"), is(false));
        assertThat(type.match("☑ done"), is(false));
        assertThat(type.match("❯ bookmark"), is(false));
    }

    @Test
    public void verifyActions() {
        assertSetContainsExactly(MessageType.LOG.getActions());
        assertSetContainsExactly(MessageType.INFO.getActions());
        assertSetContainsExactly(MessageType.TODO.getActions(), "close");
        assertSetContainsExactly(MessageType.DONE.getActions(), "open");
        assertSetContainsExactly(MessageType.BOOKMARK.getActions());
        assertSetContainsExactly(MessageType.SNIPPET.getActions());
    }

    @Test
    public void verifyCallUnknownAction() {
        assertThat(MessageType.LOG.event("invalid"), equalTo(MessageType.LOG));
        assertThat(MessageType.INFO.event("invalid"), equalTo(MessageType.INFO));
        assertThat(MessageType.TODO.event("invalid"), equalTo(MessageType.TODO));
        assertThat(MessageType.DONE.event("invalid"), equalTo(MessageType.DONE));
        assertThat(MessageType.BOOKMARK.event("invalid"), equalTo(MessageType.BOOKMARK));
        assertThat(MessageType.SNIPPET.event("invalid"), equalTo(MessageType.SNIPPET));
    }

    @Test
    public void verifyCallAction() {
        assertThat(MessageType.TODO.event("close"), equalTo(MessageType.DONE));
        assertThat(MessageType.DONE.event("open"), equalTo(MessageType.TODO));
    }

    @Test
    public void verifyCleanBody() {
        assertThat(MessageType.LOG.clearMessage("message"), equalTo("message"));
        assertThat(MessageType.LOG.clearMessage("- log"), equalTo("log"));
        assertThat(MessageType.LOG.clearMessage("≣ log"), equalTo("log"));
        assertThat(MessageType.INFO.clearMessage("message"), equalTo("message"));
        assertThat(MessageType.INFO.clearMessage("i info"), equalTo("info"));
        assertThat(MessageType.INFO.clearMessage("I info"), equalTo("info"));
        assertThat(MessageType.INFO.clearMessage("ℹ info"), equalTo("info"));
        assertThat(MessageType.TODO.clearMessage("message"), equalTo("message"));
        assertThat(MessageType.TODO.clearMessage("[] todo"), equalTo("todo"));
        assertThat(MessageType.TODO.clearMessage("[ ] todo"), equalTo("todo"));
        assertThat(MessageType.TODO.clearMessage("☐ todo"), equalTo("todo"));
        assertThat(MessageType.DONE.clearMessage("message"), equalTo("message"));
        assertThat(MessageType.DONE.clearMessage("[x] done"), equalTo("done"));
        assertThat(MessageType.DONE.clearMessage("[X] done"), equalTo("done"));
        assertThat(MessageType.DONE.clearMessage("☑ done"), equalTo("done"));
        assertThat(MessageType.BOOKMARK.clearMessage("message"), equalTo("message"));
        assertThat(MessageType.BOOKMARK.clearMessage("> bookmark"), equalTo("bookmark"));
        assertThat(MessageType.BOOKMARK.clearMessage("❯ bookmark"), equalTo("bookmark"));
        assertThat(MessageType.SNIPPET.clearMessage("message"), equalTo("message"));
        assertThat(MessageType.SNIPPET.clearMessage("$ snippet"), equalTo("snippet"));
        assertThat(MessageType.SNIPPET.clearMessage("¶ snippet"), equalTo("snippet"));
    }

    private void assertSetContainsExactly(Set<String> set, String... elements) {
        assertThat(set.size(), is(elements.length));
        for (String element : elements) {
            assertThat(set, hasItem(element));
        }
    }
}
